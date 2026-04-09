package expo.modules.datasyncnativekotlin.sdk.platform.android.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidNetworkMonitor(
    private val context: Context,
) : NetworkMonitor {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isConnected(): Boolean =
        activeCapabilities()?.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET,
        ) == true

    override fun getNetworkInfo(): NetworkInfo {
        val capabilities = activeCapabilities()

        return NetworkInfo(
            isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true,
            isValidated = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true,
            type = capabilities.toNetworkType(),
        )
    }

    override fun observeInfo(): Flow<NetworkInfo> =
        callbackFlow {
            val callback =
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        trySend(getNetworkInfo())
                    }

                    override fun onLost(network: Network) {
                        trySend(getNetworkInfo())
                    }
                }

            val request =
                NetworkRequest
                    .Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    .build()

            connectivityManager.registerNetworkCallback(request, callback)

            trySend(getNetworkInfo())

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }

    private fun activeCapabilities(): NetworkCapabilities? {
        val activeNetwork = connectivityManager.activeNetwork
        return activeNetwork?.let(connectivityManager::getNetworkCapabilities)
    }

    private fun NetworkCapabilities?.toNetworkType(): NetworkType =
        when {
            this == null -> NetworkType.UNKNOWN
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            else -> NetworkType.UNKNOWN
        }
}
