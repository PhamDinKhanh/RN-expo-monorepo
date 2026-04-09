package expo.modules.datasyncnativekotlin.bridge.expo.modules

import expo.modules.datasyncnativekotlin.sdk.api.DataSyncSdkFactory
import expo.modules.kotlin.functions.Coroutine
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class NativeFeatureFlagModule : Module() {
    override fun definition() =
        ModuleDefinition {
            Name("NativeFeatureFlagModule")

            AsyncFunction("syncFlags") Coroutine {
                dataSyncSdk().syncFlags()
            }

            Function("isFeatureEnabled") { featureKey: String, defaultValue: Boolean? ->
                dataSyncSdk().isFeatureEnabled(featureKey, defaultValue ?: false)
            }

            Function("getAllFlags") {
                dataSyncSdk().getAllFlags()
            }
        }

    private fun dataSyncSdk() = DataSyncSdkFactory.create(requireNotNull(appContext.reactContext))
}
