package expo.modules.datasyncnativekotlin.di

import expo.modules.datasyncnativekotlin.di.provider.provideOkHttpClient
import expo.modules.datasyncnativekotlin.di.provider.provideOutboxDao
import expo.modules.datasyncnativekotlin.di.provider.providePokemonDao
import expo.modules.datasyncnativekotlin.di.provider.provideRetrofit
import expo.modules.datasyncnativekotlin.di.provider.provideRoomDatabase
import expo.modules.datasyncnativekotlin.sdk.api.DataSyncConfig
import expo.modules.datasyncnativekotlin.sdk.api.DataSyncSdk
import expo.modules.datasyncnativekotlin.sdk.api.DefaultDataSyncSdk
import expo.modules.datasyncnativekotlin.sdk.api.DefaultFeatureFlagsApi
import expo.modules.datasyncnativekotlin.sdk.api.DefaultNetworkApi
import expo.modules.datasyncnativekotlin.sdk.api.FeatureFlagsApi
import expo.modules.datasyncnativekotlin.sdk.api.NetworkApi
import expo.modules.datasyncnativekotlin.sdk.api.NfcApi
import expo.modules.datasyncnativekotlin.sdk.application.facade.PokemonCatalogFacade
import expo.modules.datasyncnativekotlin.sdk.application.port.FeatureFlagManager
import expo.modules.datasyncnativekotlin.sdk.application.usecase.GetPokemonListUseCase
import expo.modules.datasyncnativekotlin.sdk.data.remote.api.PokemonApiService
import expo.modules.datasyncnativekotlin.sdk.data.repository.PokemonRepositoryImpl
import expo.modules.datasyncnativekotlin.sdk.data.transaction.RoomTransactionRunner
import expo.modules.datasyncnativekotlin.sdk.data.transaction.TransactionRunner
import expo.modules.datasyncnativekotlin.sdk.domain.repository.PokemonRepository
import expo.modules.datasyncnativekotlin.sdk.platform.android.flags.FeatureFlagManagerImpl
import expo.modules.datasyncnativekotlin.sdk.platform.android.network.AndroidNetworkMonitor
import expo.modules.datasyncnativekotlin.sdk.platform.android.network.NetworkMonitor
import expo.modules.datasyncnativekotlin.sdk.platform.android.nfc.AndroidNfcManagerImpl
import expo.modules.datasyncnativekotlin.sdk.platform.android.nfc.CurrentActivityProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

fun coreModule(config: DataSyncConfig) =
    module {
        single<DataSyncConfig> { config }

        single<FeatureFlagManager> { FeatureFlagManagerImpl() }
        single<FeatureFlagsApi> { DefaultFeatureFlagsApi(get()) }

        single { provideRoomDatabase(androidContext(), get()) }
        single { providePokemonDao(get()) }
        single { provideOutboxDao(get()) }
        single<TransactionRunner> { RoomTransactionRunner(get()) }

        single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
        single<NetworkApi> { DefaultNetworkApi(get()) }
        single { provideOkHttpClient(get()) }
        single { provideRetrofit(get(), get()) }

        single { CurrentActivityProvider(androidContext()) }
        single<NfcApi> { AndroidNfcManagerImpl(androidContext(), get()) }
    }

val dataModule =
    module {
        single<PokemonApiService> {
            get<Retrofit>().create(PokemonApiService::class.java)
        }
        single<PokemonRepository> { PokemonRepositoryImpl(get(), get(), get(), get()) }
        factory { GetPokemonListUseCase(get()) }
        factory { PokemonCatalogFacade(get()) }
        single<DataSyncSdk> { DefaultDataSyncSdk(get(), get(), get(), get()) }
    }
