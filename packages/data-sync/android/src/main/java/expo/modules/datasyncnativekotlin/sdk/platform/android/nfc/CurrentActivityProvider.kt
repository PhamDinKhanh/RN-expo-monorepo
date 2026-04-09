package expo.modules.datasyncnativekotlin.sdk.platform.android.nfc

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

class CurrentActivityProvider(
    context: Context,
) : Application.ActivityLifecycleCallbacks {
    private val application = context.applicationContext as Application
    private var activity: Activity? = null

    init {
        application.registerActivityLifecycleCallbacks(this)
    }

    fun currentActivity(): Activity? = activity

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?,
    ) {
        this.activity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        this.activity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        this.activity = activity
    }

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle,
    ) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        if (this.activity === activity) {
            this.activity = null
        }
    }
}
