package expo.modules.datasyncnativekotlin.presentation.modules

import expo.modules.datasyncnativekotlin.di.KoinInitializer
import expo.modules.datasyncnativekotlin.domain.manager.FeatureFlagManager
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NativeFeatureFlagModule : Module(), KoinComponent {

    private val featureFlagManager: FeatureFlagManager by inject()

    private val moduleScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun definition() = ModuleDefinition {
        Name("NativeFeatureFlagModule")

        OnCreate {
            KoinInitializer.start(appContext.reactContext!!)
        }

        AsyncFunction("syncFlags") {
            moduleScope.launch {
                featureFlagManager.syncFlagsFromServer()
            }
        }

        // Hàm Đọc 1 Cờ
        Function("isFeatureEnabled") { featureKey: String, defaultValue: Boolean? ->
            return@Function featureFlagManager.isFeatureEnabled(featureKey, defaultValue ?: false)
        }

        // Hàm Lấy Toàn Bộ Cờ
        Function("getAllFlags") {
            return@Function featureFlagManager.getAllFlags()
        }
    }
}