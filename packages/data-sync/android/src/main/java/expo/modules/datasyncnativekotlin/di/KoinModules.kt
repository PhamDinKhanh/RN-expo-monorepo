package expo.modules.datasyncnativekotlin.di

import expo.modules.datasyncnativekotlin.di.provider.provideOkHttpClient
import expo.modules.datasyncnativekotlin.di.provider.providePokemonDao
import expo.modules.datasyncnativekotlin.di.provider.provideRetrofit
import expo.modules.datasyncnativekotlin.di.provider.provideRoomDatabase
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
import expo.modules.datasyncnativekotlin.sdk.domain.repository.PokemonRepository
import expo.modules.datasyncnativekotlin.sdk.platform.android.flags.FeatureFlagManagerImpl
import expo.modules.datasyncnativekotlin.sdk.platform.android.network.AndroidNetworkMonitor
import expo.modules.datasyncnativekotlin.sdk.platform.android.network.NetworkMonitor
import expo.modules.datasyncnativekotlin.sdk.platform.android.nfc.CurrentActivityProvider
import expo.modules.datasyncnativekotlin.sdk.platform.android.nfc.AndroidNfcManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

val coreModule = module {
    // 1. FeatureFlag Module
    single<FeatureFlagManager> { FeatureFlagManagerImpl(androidContext()) }
    single<FeatureFlagsApi> { DefaultFeatureFlagsApi(get()) }

    // 2. Database Module
    single { provideRoomDatabase(androidContext()) }
    single { providePokemonDao(get()) }
    // 3. Network Module
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
    single<NetworkApi> { DefaultNetworkApi(get()) }
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }

    //4. NFC Module
    single { CurrentActivityProvider() }
    single<NfcApi> { AndroidNfcManagerImpl(androidContext(), get()) }

}

val dataModule = module {
    // 5. Thêm Data & Presentation Layer
    // Koin sẽ tự động lấy ApiService và PokemonDao ở trên để bơm vào đây
    // 🔥 BỔ SUNG DÒNG NÀY: Dạy Koin cách tạo ApiService từ Retrofit
    single<PokemonApiService> {
        get<Retrofit>().create(PokemonApiService::class.java)
    }
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }
    factory { GetPokemonListUseCase(get()) }
    factory { PokemonCatalogFacade(get()) }
    single<DataSyncSdk> { DefaultDataSyncSdk(get()) }
}
