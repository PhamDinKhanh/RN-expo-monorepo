package expo.modules.datasyncnativekotlin.di

import android.content.Context
import expo.modules.datasyncnativekotlin.sdk.api.DataSyncConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin

object KoinInitializer {
    fun start(
        context: Context,
        config: DataSyncConfig = DataSyncConfig(),
    ) {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(context.applicationContext)
                modules(coreModule(config), dataModule)
            }
        }
    }
}
