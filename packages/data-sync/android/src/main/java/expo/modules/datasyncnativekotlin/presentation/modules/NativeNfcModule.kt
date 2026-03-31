package expo.modules.datasyncnativekotlin.presentation.modules

import expo.modules.datasyncnativekotlin.di.KoinInitializer
import expo.modules.datasyncnativekotlin.domain.manager.AndroidNfcManager
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NativeNfcModule : Module(), KoinComponent {

    private val nfcManager: AndroidNfcManager by inject()

    override fun definition() = ModuleDefinition {
        Name("NativeNfcModule")

        Events("onNfcTagScanned")

        OnCreate {
            KoinInitializer.start(appContext.reactContext!!)
        }

        Function("startNfcReader") {
            // Lấy Activity hiện tại của Tablet
            val activity = appContext.currentActivity ?: throw Exception("Không tìm thấy Activity")


            nfcManager.startListening(activity) { tagData ->
                // Khi có thẻ quẹt, bắn event thẳng lên React Native
                sendEvent(
                    "onNfcTagScanned", mapOf(
                        "tagId" to tagData,
                        "timestamp" to System.currentTimeMillis()
                    )
                )
            }
        }

        Function("stopNfcReader") {
            val activity = appContext.currentActivity ?: throw Exception("Không tìm thấy Activity")

            nfcManager.stopListening(activity)
        }
    }
}