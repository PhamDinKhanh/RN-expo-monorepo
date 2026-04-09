# data-sync-native-kotlin

`@sw/data-sync-native-kotlin` is an Expo module with a native Android implementation under `packages/data-sync/android`.

The Android code already follows a layered shape:

- `bridge/expo` exposes native features to JavaScript through Expo modules.
- `sdk/api` is the public Kotlin-facing surface used by the bridge.
- `sdk/application` contains orchestration, use cases, and the interfaces those workflows depend on.
- `sdk/data` owns Room, Retrofit, mapping, and repository implementations.
- `sdk/domain` contains core models, repository contracts, and shared exceptions.
- `sdk/platform/android` contains Android-only integrations such as connectivity, feature flags, and NFC.
- `di` is the composition root that wires the module together with Koin.

## Current Android Structure

Source root:

```text
packages/data-sync/android/
  build.gradle
  src/main/AndroidManifest.xml
  src/main/java/expo/modules/datasyncnativekotlin/
    bridge/
      expo/
        dto/
        mapper/
        modules/
    di/
      provider/
    sdk/
      api/
      application/
        facade/
        port/
        usecase/
      data/
        local/
          dao/
          database/
          entities/
          migration/
        mapper/
        remote/
          api/
          dto/
          interceptor/
        repository/
        transaction/
      domain/
        exception/
        model/
        repository/
      platform/
        android/
          flags/
          network/
          nfc/
  src/test/java/...
```

Non-source folders such as `android/build` and `android/.gradle` are generated artifacts. They should not be used to understand behavior and should not be documented as part of the architecture.

## What Each Area Does Today

### `bridge/expo`

This is the JavaScript entry layer.

- `modules/NativeDataSyncModule.kt` exposes `fetchPokemons`.
- `modules/NativeFeatureFlagModule.kt` exposes feature flag reads and sync.
- `modules/NativeNetworkModule.kt` exposes connectivity state and emits `networkChanged`.
- `modules/NativeNfcModule.kt` starts NFC reader mode and emits `onNfcTagScanned`.
- `dto` and `mapper` translate SDK models into JS-safe payloads.

The bridge is thin: each module calls `DataSyncSdkFactory.create(...)` and forwards to the SDK.

### `sdk/api`

This is the stable entry point for Kotlin callers.

- `DataSyncSdk.kt` defines the public API.
- `DefaultDataSyncSdk.kt` delegates to facade and platform APIs.
- `DataSyncConfig.kt` carries `baseUrl`, `databaseName`, and `enableLogging`, including the default backend base URL.
- `DataSyncSdkFactory.kt` initializes Koin and returns the singleton `DataSyncSdk`.
- `FeatureFlagsApi`, `NetworkApi`, and `NfcApi` separate feature surfaces from implementations.

If a new capability should be available to Expo, it should usually appear here first.

### `sdk/application`

This layer coordinates business operations without owning persistence or Android framework code. It can also define small interfaces in `port/` when the application layer needs something implemented elsewhere.

- `usecase/GetPokemonListUseCase.kt` calls the repository.
- `facade/PokemonCatalogFacade.kt` normalizes inputs and wraps errors.
- `port/FeatureFlagManager.kt` is the interface the application layer uses to read and sync feature flags; the Android implementation lives under `sdk/platform/android/flags`.

This layer is small today, but it is where multi-step workflows should live instead of the Expo modules.

### `sdk/data`

This layer contains persistence, network access, mapping, and repository implementations.

- `local/database/AppDatabase.kt` defines the Room database.
- `local/dao/*` contains DAO contracts for Pokemon and outbox storage.
- `local/entities/*` contains Room entities.
- `remote/api/PokemonApiService.kt` defines Retrofit calls.
- `remote/dto/*` contains network DTOs.
- `repository/PokemonRepositoryImpl.kt` combines remote fetches with local fallback.
- `transaction/*` wraps Room transactions behind `TransactionRunner`.
- `mapper/*` contains JSON and entity/domain mapping.

Current repository behavior:

1. `getPokemonList()` tries the remote API first.
2. On a successful response, it upserts the returned Pokemon into Room.
3. It then reads the page from Room.
4. If local data exists, it returns that data even when the network request failed.
5. If both remote and local are empty, it returns a failure.

`savePokemonWithEvent()` writes the Pokemon locally and adds an outbox event only when the write did not come from sync.

### `sdk/domain`

This is the smallest and most stable layer.

- `model/Pokemon.kt` and related models represent core data.
- `repository/PokemonRepository.kt` defines repository contracts.
- `exception/*` contains shared native exceptions such as NFC and activity lookup failures.

This layer should stay free of Room, Retrofit, Koin, Expo, and Android framework dependencies.

### `sdk/platform/android`

This layer contains Android-specific integrations and concrete implementations of interfaces owned by higher layers.

- `network/AndroidNetworkMonitor.kt` uses `ConnectivityManager` and exposes current state plus a `Flow`.
- `flags/FeatureFlagManagerImpl.kt` is the Android implementation of the application-layer `FeatureFlagManager` interface; it currently serves an in-memory default flag map and has a placeholder sync implementation.
- `nfc/AndroidNfcManagerImpl.kt` enables reader mode on the current activity and returns scanned tag IDs as hex strings.
- `nfc/CurrentActivityProvider.kt` resolves the activity required by NFC reader mode.

These classes are allowed to depend on Android APIs directly.

### `di`

`di/KoinInitializer.kt` and `di/KoinModules.kt` start Koin once and wire the graph:

- config
- Room database and DAOs
- transaction runner
- network monitor
- OkHttp and Retrofit
- feature flag manager
- NFC manager
- repository, use case, facade, and `DataSyncSdk`

Provider functions under `di/provider` keep construction details out of the main module definitions. For example, `di/provider/NetworkProvider.kt` builds `OkHttpClient` and `Retrofit` from `DataSyncConfig`.

## Runtime Behavior

The common runtime path is:

```text
JS wrapper
  -> Expo module in bridge/expo/modules
  -> DataSyncSdkFactory.create(context)
  -> DataSyncSdk
  -> application facade / feature API / platform API
  -> repository or Android integration
```

Concrete examples:

- Pokemon fetch: JS -> `NativeDataSyncModule` -> `DataSyncSdk.fetchPokemons()` -> `PokemonCatalogFacade` -> `GetPokemonListUseCase` -> `PokemonRepositoryImpl` -> Retrofit + Room.
- Network events: JS subscribes -> `NativeNetworkModule` collects `observeNetworkStatus()` -> `AndroidNetworkMonitor` pushes `networkChanged` events.
- NFC scan: JS starts reader -> `NativeNfcModule` -> `AndroidNfcManagerImpl.startSession()` -> Android reader callback -> `onNfcTagScanned` event.
- Feature flags: JS calls `NativeFeatureFlagModule` -> `DefaultFeatureFlagsApi` -> `FeatureFlagManagerImpl`, which currently reads from an in-memory cache.

## Android Build Notes

`android/build.gradle` shows the main native dependencies in use today:

- Koin for dependency injection
- Room + KSP for local storage
- SQLCipher dependencies are present, but the current Room provider does not yet configure encrypted storage
- Retrofit + OkHttp for network access
- WorkManager dependency is present, but there is no worker implementation in the current source tree
- Coroutines and Kotlin serialization

This matters for contributors: the dependency list is slightly ahead of the implemented feature set, so prefer the source tree over Gradle dependencies when deciding what behavior already exists.

## Android Kotlin Quality Checks

The Android module also enforces Kotlin style and static analysis through:

- `ktlint` via `org.jlleitschuh.gradle.ktlint`
- `detekt` via `io.gitlab.arturbosch.detekt`

These checks are configured in `packages/data-sync/android/build.gradle` and run as part of the module `check` task.

The module is included in the consuming Android app as Gradle project `:sw-data-sync-native-kotlin`.

Enforcement path:

- `check` depends on both `ktlintCheck` and `detekt`.
- `build` includes `check` in the standard Gradle lifecycle.
- CI should treat `:sw-data-sync-native-kotlin:check` or `:sw-data-sync-native-kotlin:build` as the required quality gate.

Run the checks from `apps/tablet-app/android`:

```powershell
.\gradlew.bat :sw-data-sync-native-kotlin:ktlintCheck
.\gradlew.bat :sw-data-sync-native-kotlin:ktlintFormat
.\gradlew.bat :sw-data-sync-native-kotlin:detekt
.\gradlew.bat :sw-data-sync-native-kotlin:check
.\gradlew.bat :sw-data-sync-native-kotlin:build
```

What each task does:

- `ktlintCheck` validates Kotlin formatting.
- `ktlintFormat` auto-formats issues that ktlint can fix safely.
- `detekt` reports Kotlin code smells and static-analysis findings.
- `check` runs the standard module verification lifecycle and depends on both `ktlintCheck` and `detekt`.

Contributor expectations:

- Run `ktlintFormat` before making larger Kotlin changes.
- Keep `detekt` warnings actionable instead of disabling rules broadly.
- Exclude generated outputs such as `build/`, `.gradle/`, and generated implementation files from quality review.
- When a rule needs to be relaxed, prefer a scoped config change in `packages/data-sync/android/detekt.yml` over local suppression noise.

## Contributor Guidance

Use the Android contributor guide for day-to-day edits: [android/CONTRIBUTING.md](./android/CONTRIBUTING.md).

Short version:

- Add public capabilities at `sdk/api` first.
- Keep Expo modules thin and mapping-focused.
- Put orchestration in `sdk/application`.
- Put persistence and remote access in `sdk/data`.
- Put Android framework code in `sdk/platform/android`.
- Wire new implementations in `di`.
- Add or update tests when repository or facade behavior changes.
