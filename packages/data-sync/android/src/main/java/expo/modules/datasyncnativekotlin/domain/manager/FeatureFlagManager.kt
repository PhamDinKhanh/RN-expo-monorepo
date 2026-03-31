package expo.modules.datasyncnativekotlin.domain.manager

interface FeatureFlagManager {
    /**
     * Kiểm tra xem một tính năng có đang được bật hay không.
     */
    fun isFeatureEnabled(featureKey: String, defaultValue: Boolean = false): Boolean

    /**
     * Lấy toàn bộ danh sách cờ hiện tại để gửi lên JS.
     */
    fun getAllFlags(): Map<String, Boolean>

    /**
     * Kích hoạt đồng bộ cờ từ Server (chạy ngầm).
     */
    suspend fun syncFlagsFromServer()
}