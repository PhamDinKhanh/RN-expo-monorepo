package expo.modules.datasyncnativekotlin.sdk.platform.android.network

data class NetworkInfo(
    val isConnected: Boolean,
    val isValidated: Boolean,
    val type: NetworkType,
)

enum class NetworkType {
    WIFI,
    CELLULAR,
    UNKNOWN,
}
