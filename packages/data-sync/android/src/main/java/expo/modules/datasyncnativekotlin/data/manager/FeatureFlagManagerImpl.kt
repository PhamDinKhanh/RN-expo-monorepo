package expo.modules.datasyncnativekotlin.data.manager

import android.content.Context
import expo.modules.datasyncnativekotlin.domain.manager.FeatureFlagManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeatureFlagManagerImpl(private val context: Context) : FeatureFlagManager {

    private val defaultFlags = mapOf(
        "enable_offline_sync" to true,
        "new_checkout_ui" to false,
        "enable_bluetooth_printer" to true
    )

    private var flagsCache = defaultFlags.toMutableMap()

    override fun isFeatureEnabled(
        featureKey: String,
        defaultValue: Boolean
    ): Boolean {
        return flagsCache[featureKey] ?: defaultValue
    }

    override fun getAllFlags(): Map<String, Boolean> {
        return flagsCache.toMap()
    }

    override suspend fun syncFlagsFromServer() {
        withContext(Dispatchers.IO) {
            try {
                // 1. Gọi API lấy cấu hình mới nhất
                // val remoteFlags = apiService.fetchFeatureFlags()

                // 2. Cập nhật lại Cache trên RAM
                // flagsCache.clear()
                // flagsCache.putAll(remoteFlags)

                println("Đã đồng bộ Feature Flags từ Server thành công!")
            } catch (e: Exception) {
                println("Lỗi khi đồng bộ Feature Flags: ${e.message}")
            }
        }
    }
}