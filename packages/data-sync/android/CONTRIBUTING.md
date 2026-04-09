# Android Contributor Guide

This guide describes how to work safely inside `packages/data-sync/android` as it exists today.

## Read This Structure First

Only the source tree under `src/` should drive architecture decisions.

```text
android/
  build.gradle
  src/main/java/expo/modules/datasyncnativekotlin/
    bridge/expo
    di
    sdk/api
    sdk/application
    sdk/data
    sdk/domain
    sdk/platform/android
  src/test/java
```

Ignore `build/` and `.gradle/` when reviewing structure. They are generated outputs.

## Practical Behavior Rules

### 1. Start from the caller and follow the real flow

Most features enter through Expo modules:

- `NativeDataSyncModule`
- `NativeFeatureFlagModule`
- `NativeNetworkModule`
- `NativeNfcModule`

From there the call should move into `sdk/api`, then into either:

- `sdk/application` for orchestration
- `sdk/data` for storage/network-backed behavior
- `sdk/platform/android` for Android framework integrations

If you find yourself adding business logic directly to an Expo module, stop and move it down a layer.

### 2. Change the public API before the bridge

When exposing a new native capability:

1. Add or extend the API in `sdk/api`.
2. Implement the behavior below that API.
3. Wire it in `di`.
4. Expose it from `bridge/expo/modules`.
5. Add mapping code in `bridge/expo/dto` or `bridge/expo/mapper` if JS needs a different shape.

This keeps the bridge from becoming the real architecture.

### 3. Respect the current layer boundaries

- `bridge/expo` may depend on `sdk/api` and bridge mappers.
- `sdk/application` may depend on domain contracts and repository interfaces.
- `sdk/data` may depend on Room, Retrofit, DAOs, DTOs, and mappers.
- `sdk/platform/android` may depend on Android framework APIs.
- `sdk/domain` should remain the cleanest layer and avoid Android, Room, Retrofit, Koin, and Expo imports.
- `di` is the only place that should know how the object graph is assembled.

### 4. Preserve the current Pokemon repository behavior unless intentionally changing it

`PokemonRepositoryImpl` currently behaves as an offline-friendly read path:

1. Try remote fetch.
2. If remote succeeds, upsert into Room.
3. Read the requested page from Room.
4. Return local data if it exists.
5. Fail only when both remote and local are empty.

If you change this flow, update tests and document the new behavior in the package README.

### 5. Treat feature flags and NFC as partially implemented integrations

Current status:

- Feature flags are backed by an in-memory default map in `FeatureFlagManagerImpl`.
- `syncFlagsFromServer()` is still a placeholder.
- NFC depends on `CurrentActivityProvider` and throws when there is no activity or no NFC adapter.

Contributors should not document these as server-backed or production-complete until the code actually does that work.

### 6. Keep DI changes explicit

The dependency graph is split across:

- `KoinInitializer.kt`
- `KoinModules.kt`
- `di/provider/*`

When adding a service:

1. Create the implementation in the correct layer.
2. Bind it in Koin.
3. Prefer a provider function when construction needs several framework objects or configuration values.

### 7. Test behavior where it lives

Current tests are under `src/test/java` and already cover `PokemonRepositoryImpl`.

Preferred test placement:

- repository behavior: `src/test/java/.../sdk/data/...`
- facade or use case behavior: `src/test/java/.../sdk/application/...`
- pure mapper logic: unit tests next to the owning layer

Add tests when you change fallback behavior, transaction behavior, mapping, or public SDK semantics.

## Common Change Patterns

### Add a new JS-exposed native read operation

1. Extend `DataSyncSdk`.
2. Implement it in `DefaultDataSyncSdk`.
3. Add or reuse an application facade/use case.
4. Implement repository or platform work below it.
5. Register dependencies in Koin.
6. Expose it in the relevant Expo module.
7. Add mapper/DTO code only if the JS shape differs.

### Add a new Android integration

Examples: background jobs, secure storage, Bluetooth, sensor access.

1. Put Android-specific code under `sdk/platform/android/<feature>`.
2. Expose a narrow interface through `sdk/api` or an application port.
3. Keep framework classes out of `sdk/domain`.
4. Bind the implementation in `di`.

### Change local persistence

1. Update entities and DAO contracts in `sdk/data/local`.
2. Update `AppDatabase`.
3. Add a real migration when needed.
4. Update repository mapping and tests.

Note: `fallbackToDestructiveMigration()` is currently enabled in the Room provider, so schema changes can wipe local data during development unless that behavior is intentionally replaced.

## Current Gaps Contributors Should Know About

- `sdk/data/local/migration/MigrationV2.kt` exists, but `AppDatabase` is still version `1`, so the migration is not active.
- WorkManager is declared in Gradle, but there is no worker package in the current source tree.
- SQLCipher dependencies are present, but the current database provider does not configure encrypted Room.
- Some comments and log strings are still draft-level and not fully normalized.

Treat these as implementation gaps, not finished capabilities.
