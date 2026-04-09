package expo.modules.datasyncnativekotlin.sdk.application.port

interface FeatureFlagManager {
    fun isFeatureEnabled(
        featureKey: String,
        defaultValue: Boolean = false,
    ): Boolean

    fun getAllFlags(): Map<String, Boolean>

    suspend fun syncFlagsFromServer()
}
