package expo.modules.datasyncnativekotlin.sdk.api

import android.content.Context
import expo.modules.datasyncnativekotlin.di.KoinInitializer
import org.koin.java.KoinJavaComponent.get

object DataSyncSdkFactory {
    fun create(
        context: Context,
        config: DataSyncConfig = DataSyncConfig(),
    ): DataSyncSdk {
        KoinInitializer.start(context, config)
        return get(DataSyncSdk::class.java)
    }
}
