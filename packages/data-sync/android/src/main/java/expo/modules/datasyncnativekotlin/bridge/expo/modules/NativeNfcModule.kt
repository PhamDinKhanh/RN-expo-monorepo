package expo.modules.datasyncnativekotlin.bridge.expo.modules

import expo.modules.datasyncnativekotlin.di.KoinInitializer
import expo.modules.datasyncnativekotlin.sdk.api.NfcApi
import expo.modules.datasyncnativekotlin.sdk.platform.android.nfc.CurrentActivityProvider
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NativeNfcModule : Module(), KoinComponent {

    private val nfcApi: NfcApi by inject()
    private val currentActivityProvider: CurrentActivityProvider by inject()

    override fun definition() = ModuleDefinition {
        Name("NativeNfcModule")

        Events("onNfcTagScanned")

        OnCreate {
            KoinInitializer.start(appContext.reactContext!!)
            currentActivityProvider.update(appContext.currentActivity)
        }

        AsyncFunction("startNfcReader") {
            currentActivityProvider.update(appContext.currentActivity)
            val result = nfcApi.startSession { tagData ->
                sendEvent(
                    "onNfcTagScanned", mapOf(
                        "tagId" to tagData,
                        "timestamp" to System.currentTimeMillis()
                    )
                )
            }

            return@AsyncFunction result
        }

        Function("stopNfcReader") {
            currentActivityProvider.update(appContext.currentActivity)
            nfcApi.stopSession()
        }
    }
}
