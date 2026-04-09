package expo.modules.datasyncnativekotlin.sdk.api

import expo.modules.datasyncnativekotlin.sdk.platform.android.network.NetworkInfo
import expo.modules.datasyncnativekotlin.sdk.platform.android.network.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultNetworkApi(
    private val networkMonitor: NetworkMonitor,
) : NetworkApi {
    override fun isConnected(): Boolean = networkMonitor.isConnected()

    override fun getNetworkStatus(): NetworkStatus = networkMonitor.getNetworkInfo().toApiModel()

    override fun observeStatus(): Flow<NetworkStatus> = networkMonitor.observeInfo().map { it.toApiModel() }
}

private fun NetworkInfo.toApiModel(): NetworkStatus =
    NetworkStatus(
        isConnected = isConnected,
        isValidated = isValidated,
        type = type.name,
    )
