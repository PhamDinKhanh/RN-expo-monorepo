package expo.modules.datasyncnativekotlin.sdk.platform.android.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidNetworkMonitor(private val context: Context) : NetworkMonitor {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun getNetworkInfo(): NetworkInfo {
        val network = connectivityManager.activeNetwork ?: return NetworkInfo(
            isConnected = false,
            isValidated = false,
            type = NetworkType.UNKNOWN
        )

        val capabilities = connectivityManager.getNetworkCapabilities(network)
            ?: return NetworkInfo(
                isConnected = false,
                isValidated = false,
                type = NetworkType.UNKNOWN
            )

        val isConnected =
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        val isValidated =
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        val type = when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            else -> NetworkType.UNKNOWN
        }

        return NetworkInfo(
            isConnected = isConnected,
            isValidated = isValidated,
            type = type
        )
    }

    override fun observeInfo(): Flow<NetworkInfo>  = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                trySend(getNetworkInfo())
            }

            override fun onLost(network: Network) {
                trySend(getNetworkInfo())
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        trySend(getNetworkInfo())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

}
