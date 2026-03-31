package expo.modules.datasyncnativekotlin.data.manager

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import expo.modules.datasyncnativekotlin.domain.manager.AndroidNfcManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AndroidNfcManagerImpl(context: Context) : AndroidNfcManager, NfcAdapter.ReaderCallback {

    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)
    private var onTagReadCallback: ((String) -> Unit)? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun startListening(
        activity: Activity,
        onTagRead: (String) -> Unit
    ) {
        if (nfcAdapter == null || !nfcAdapter.isEnabled) {
            println("NFC không được hỗ trợ hoặc đang bị tắt!")
            return
        }

        this.onTagReadCallback = onTagRead

        // Cấu hình cờ để đọc đa dạng các loại thẻ (Thẻ từ, thẻ chip, thẻ xe...)
        val flags = NfcAdapter.FLAG_READER_NFC_A or
                NfcAdapter.FLAG_READER_NFC_B or
                NfcAdapter.FLAG_READER_NFC_F or
                NfcAdapter.FLAG_READER_NFC_V

        // Bật Reader Mode: Bắt toàn bộ thẻ quẹt qua mà không hiện Popup của Android
        nfcAdapter.enableReaderMode(activity, this, flags, null)
    }

    override fun stopListening(activity: Activity) {
        nfcAdapter?.disableReaderMode(activity)
        this.onTagReadCallback = null
    }

    override fun onTagDiscovered(tag: Tag?) {
        tag?.let {
            // Ví dụ: Lấy mã ID vật lý của thẻ (UID)
            val tagIdBytes = it.id
            val tagIdHex = tagIdBytes.joinToString("") { byte -> "%02X".format(byte) }

            // Xử lý đọc NDEF (dữ liệu Text/URL lưu trong thẻ) nếu cần
            /*
                val ndef = Ndef.get(it)
                ndef?.connect()
                val message = ndef?.cachedNdefMessage
                // ... Parse message ở đây
                ndef?.close()
                */

            // Đẩy data ra Main Thread để bắn lên giao diện an toàn
            mainScope.launch {
                onTagReadCallback?.invoke(tagIdHex)
            }
        }
    }
}