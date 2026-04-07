package expo.modules.datasyncnativekotlin.core.network

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {

    fun isConnected(): Boolean

    fun getNetworkInfo(): NetworkInfo

    fun observeInfo(): Flow<NetworkInfo>
}