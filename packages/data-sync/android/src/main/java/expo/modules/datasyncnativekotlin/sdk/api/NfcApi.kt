package expo.modules.datasyncnativekotlin.sdk.api

interface NfcApi {
    fun startSession(onTagRead: (String) -> Unit): Boolean

    fun stopSession()
}
