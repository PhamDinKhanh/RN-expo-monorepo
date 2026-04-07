package expo.modules.datasyncnativekotlin.sdk.platform.android.nfc

import android.app.Activity

class CurrentActivityProvider {
    private var activity: Activity? = null

    fun update(activity: Activity?) {
        this.activity = activity
    }

    fun currentActivity(): Activity? = activity
}
