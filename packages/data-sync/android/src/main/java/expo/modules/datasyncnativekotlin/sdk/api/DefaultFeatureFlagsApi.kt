package expo.modules.datasyncnativekotlin.sdk.api

import expo.modules.datasyncnativekotlin.sdk.application.port.FeatureFlagManager

class DefaultFeatureFlagsApi(
    private val featureFlagManager: FeatureFlagManager,
) : FeatureFlagsApi {
    override fun isFeatureEnabled(
        featureKey: String,
        defaultValue: Boolean,
    ): Boolean = featureFlagManager.isFeatureEnabled(featureKey, defaultValue)

    override fun getAllFlags(): Map<String, Boolean> = featureFlagManager.getAllFlags()

    override suspend fun syncFlags() {
        featureFlagManager.syncFlagsFromServer()
    }
}
