package expo.modules.datasyncnativekotlin.sdk.platform.android.nfc

import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import expo.modules.datasyncnativekotlin.sdk.api.NfcApi
import expo.modules.datasyncnativekotlin.sdk.domain.exception.ActivityNotFoundException
import expo.modules.datasyncnativekotlin.sdk.domain.exception.NfcNotSupportedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AndroidNfcManagerImpl(
    context: Context,
    private val currentActivityProvider: CurrentActivityProvider,
) : NfcApi,
    NfcAdapter.ReaderCallback {
    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)
    private var onTagReadCallback: ((String) -> Unit)? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun startSession(onTagRead: (String) -> Unit): Boolean {
        if (nfcAdapter == null || !nfcAdapter.isEnabled) {
            throw NfcNotSupportedException()
        }

        val activity =
            currentActivityProvider.currentActivity()
                ?: throw ActivityNotFoundException()

        this.onTagReadCallback = onTagRead

        val flags =
            NfcAdapter.FLAG_READER_NFC_A or
                NfcAdapter.FLAG_READER_NFC_B or
                NfcAdapter.FLAG_READER_NFC_F or
                NfcAdapter.FLAG_READER_NFC_V

        nfcAdapter.enableReaderMode(activity, this, flags, null)
        return true
    }

    override fun stopSession() {
        val activity =
            currentActivityProvider.currentActivity()
                ?: throw ActivityNotFoundException()

        nfcAdapter?.disableReaderMode(activity)
        this.onTagReadCallback = null
    }

    override fun onTagDiscovered(tag: Tag?) {
        if (tag == null) return

        val tagIdHex = tag.id.joinToString("") { byte -> "%02X".format(byte) }

        mainScope.launch {
            onTagReadCallback?.invoke(tagIdHex)
        }
    }
}
