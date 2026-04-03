package expo.modules.datasyncnativekotlin.presentation.modules

import expo.modules.datasyncnativekotlin.core.network.NetworkMonitor
import expo.modules.datasyncnativekotlin.di.KoinInitializer
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NativeNetworkModule : Module(), KoinComponent {
    private val networkMonitor: NetworkMonitor by inject()
    private val moduleJob = SupervisorJob()

    private val moduleScope = CoroutineScope(Dispatchers.IO + moduleJob)

    override fun definition() = ModuleDefinition {
        Name("NativeNetworkModule")

        Events("networkChanged")

        OnCreate {
            KoinInitializer.start(appContext.reactContext!!)
        }

        Function("isConnected") {
            val isConnected = networkMonitor.isConnected()
            return@Function isConnected
        }

        AsyncFunction("getNetworkInfo") {
            val info = networkMonitor.getNetworkInfo()

            mapOf(
                "isConnected" to info.isConnected,
                "isValidated" to info.isValidated,
                "type" to info.type.name
            )
        }

        OnStartObserving {
            moduleScope.launch {
                try {
                    networkMonitor.observeInfo().collect { info ->
                        sendEvent(
                            "networkChanged", mapOf(
                                "isConnected" to info.isConnected,
                                "isValidated" to info.isValidated,
                                "type" to info.type.name
                            )
                        )
                    }
                } catch (e: Exception) {
                    println("Error observing networkChanged: ${e.message}")
                }
            }
        }

        OnDestroy {
            moduleScope.cancel("Module destroyed, cancelling all sync tasks.")
        }
    }
}