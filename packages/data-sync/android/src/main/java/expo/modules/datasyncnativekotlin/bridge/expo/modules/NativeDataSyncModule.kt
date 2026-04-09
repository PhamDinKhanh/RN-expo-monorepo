package expo.modules.datasyncnativekotlin.bridge.expo.modules

import expo.modules.datasyncnativekotlin.bridge.expo.mapper.toJSDto
import expo.modules.datasyncnativekotlin.sdk.api.DataSyncSdkFactory
import expo.modules.kotlin.functions.Coroutine
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class NativeDataSyncModule : Module() {
    override fun definition() =
        ModuleDefinition {
            Name("NativeDataSyncModule")

            AsyncFunction("fetchPokemons") Coroutine { limit: Int ->
                dataSyncSdk().fetchPokemons(limit).toJSDto()
            }
        }

    private fun dataSyncSdk() = DataSyncSdkFactory.create(requireNotNull(appContext.reactContext))
}
