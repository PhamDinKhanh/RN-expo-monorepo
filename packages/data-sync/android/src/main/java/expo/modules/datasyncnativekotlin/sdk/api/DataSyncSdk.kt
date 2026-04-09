package expo.modules.datasyncnativekotlin.sdk.api

import kotlinx.coroutines.flow.Flow

interface DataSyncSdk {
    suspend fun fetchPokemons(
        limit: Int,
        offset: Int = 0,
    ): SdkPokemonPage

    fun isFeatureEnabled(
        featureKey: String,
        defaultValue: Boolean = false,
    ): Boolean

    fun getAllFlags(): Map<String, Boolean>

    suspend fun syncFlags()

    fun isConnected(): Boolean

    fun getNetworkStatus(): NetworkStatus

    fun observeNetworkStatus(): Flow<NetworkStatus>

    fun startNfcSession(onTagRead: (String) -> Unit): Boolean

    fun stopNfcSession()
}
