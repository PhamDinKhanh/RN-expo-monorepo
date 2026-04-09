package expo.modules.datasyncnativekotlin.bridge.expo.modules

import expo.modules.datasyncnativekotlin.sdk.api.DataSyncSdkFactory
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class NativeNfcModule : Module() {
    override fun definition() =
        ModuleDefinition {
            Name("NativeNfcModule")

            Events("onNfcTagScanned")

            AsyncFunction("startNfcReader") {
                dataSyncSdk().startNfcSession { tagData ->
                    sendEvent(
                        "onNfcTagScanned",
                        mapOf(
                            "tagId" to tagData,
                            "timestamp" to System.currentTimeMillis(),
                        ),
                    )
                }
            }

            Function("stopNfcReader") {
                dataSyncSdk().stopNfcSession()
            }
        }

    private fun dataSyncSdk() = DataSyncSdkFactory.create(requireNotNull(appContext.reactContext))
}
