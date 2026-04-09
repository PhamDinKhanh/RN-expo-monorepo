package expo.modules.datasyncnativekotlin.sdk.api

interface FeatureFlagsApi {
    fun isFeatureEnabled(
        featureKey: String,
        defaultValue: Boolean = false,
    ): Boolean

    fun getAllFlags(): Map<String, Boolean>

    suspend fun syncFlags()
}
