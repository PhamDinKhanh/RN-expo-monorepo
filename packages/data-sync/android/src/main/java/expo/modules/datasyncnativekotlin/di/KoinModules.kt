package expo.modules.datasyncnativekotlin.di

import expo.modules.datasyncnativekotlin.core.network.AndroidNetworkMonitor
import expo.modules.datasyncnativekotlin.core.network.NetworkMonitor
import expo.modules.datasyncnativekotlin.data.manager.AndroidNfcManagerImpl
import expo.modules.datasyncnativekotlin.data.manager.FeatureFlagManagerImpl
import expo.modules.datasyncnativekotlin.di.provider.provideOkHttpClient
import expo.modules.datasyncnativekotlin.di.provider.provideOrderDao
import expo.modules.datasyncnativekotlin.di.provider.provideRetrofit
import expo.modules.datasyncnativekotlin.di.provider.provideRoomDatabase
import expo.modules.datasyncnativekotlin.domain.manager.AndroidNfcManager
import expo.modules.datasyncnativekotlin.domain.manager.FeatureFlagManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val coreModule = module {
    // 1. FeatureFlag Module
    single<FeatureFlagManager> { FeatureFlagManagerImpl(androidContext()) }

    // 2. Database Module
    single { provideRoomDatabase(androidContext()) }
    single { provideOrderDao(get()) }


    // 3. Network Module
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }

    //4. NFC Module
    single<AndroidNfcManager> { AndroidNfcManagerImpl(androidContext()) }
}

val dataModule = module {


}