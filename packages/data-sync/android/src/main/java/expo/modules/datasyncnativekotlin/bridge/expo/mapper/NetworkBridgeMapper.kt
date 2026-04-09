package expo.modules.datasyncnativekotlin.bridge.expo.mapper

import expo.modules.datasyncnativekotlin.sdk.api.NetworkStatus

fun NetworkStatus.toJsMap(): Map<String, Any> =
    mapOf(
        "isConnected" to isConnected,
        "isValidated" to isValidated,
        "type" to type,
    )
