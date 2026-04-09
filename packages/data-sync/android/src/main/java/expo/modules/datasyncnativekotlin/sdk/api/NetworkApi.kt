package expo.modules.datasyncnativekotlin.sdk.api

import kotlinx.coroutines.flow.Flow

interface NetworkApi {
    fun isConnected(): Boolean

    fun getNetworkStatus(): NetworkStatus

    fun observeStatus(): Flow<NetworkStatus>
}
