package expo.modules.datasyncnativekotlin.bridge.expo.modules

import expo.modules.datasyncnativekotlin.bridge.expo.mapper.toJsMap
import expo.modules.datasyncnativekotlin.sdk.api.DataSyncSdkFactory
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NativeNetworkModule : Module() {
    private val moduleJob = SupervisorJob()
    private val moduleScope = CoroutineScope(Dispatchers.IO + moduleJob)

    override fun definition() =
        ModuleDefinition {
            Name("NativeNetworkModule")

            Events("networkChanged")

            Function("isConnected") {
                dataSyncSdk().isConnected()
            }

            AsyncFunction("getNetworkInfo") {
                dataSyncSdk().getNetworkStatus().toJsMap()
            }

            OnStartObserving {
                moduleScope.launch {
                    try {
                        dataSyncSdk().observeNetworkStatus().collect { status ->
                            sendEvent("networkChanged", status.toJsMap())
                        }
                    } catch (_: Exception) {
                    }
                }
            }

            OnDestroy {
                moduleScope.cancel("Module destroyed, cancelling all sync tasks.")
            }
        }

    private fun dataSyncSdk() = DataSyncSdkFactory.create(requireNotNull(appContext.reactContext))
}
