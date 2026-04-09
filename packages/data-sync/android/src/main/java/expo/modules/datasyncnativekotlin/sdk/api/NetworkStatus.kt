package expo.modules.datasyncnativekotlin.sdk.api

data class NetworkStatus(
    val isConnected: Boolean,
    val isValidated: Boolean,
    val type: String,
)
