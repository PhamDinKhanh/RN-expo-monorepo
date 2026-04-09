package expo.modules.datasyncnativekotlin.sdk.platform.android.flags

import expo.modules.datasyncnativekotlin.sdk.application.port.FeatureFlagManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeatureFlagManagerImpl : FeatureFlagManager {
    private val defaultFlags =
        mapOf(
            "enable_offline_sync" to true,
            "new_checkout_ui" to false,
            "enable_bluetooth_printer" to true,
        )

    private var flagsCache = defaultFlags.toMutableMap()

    override fun isFeatureEnabled(
        featureKey: String,
        defaultValue: Boolean,
    ): Boolean = flagsCache[featureKey] ?: defaultValue

    override fun getAllFlags(): Map<String, Boolean> = flagsCache.toMap()

    override suspend fun syncFlagsFromServer() {
        withContext(Dispatchers.IO) {
            // Server-backed flag sync is not implemented yet.
        }
    }
}
