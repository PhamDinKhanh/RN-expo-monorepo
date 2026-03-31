package expo.modules.datasyncnativekotlin.presentation.modules

import android.content.Context
import android.os.BatteryManager
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class NativeDataSyncModule : Module() {
    private val context: Context
        get() = appContext.reactContext ?: throw Exception("React context not found")

    override fun definition() = ModuleDefinition {
        // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
        // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
        // The module will be accessible from `requireNativeModule('DataSync')` in JavaScript.
        Name("NativeDataSyncModule")

        // Defines constant property on the module.
        Constant("PI") {
            Math.PI
        }

        // Defines event names that the module can send to JavaScript.
        Events("onChange")

        // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
        Function("hello") {
            "Hello world! 👋"
        }

        // Defines a JavaScript function that always returns a Promise and whose native code
        // is by default dispatched on the different thread than the JavaScript runtime runs on.
        AsyncFunction("setValueAsync") { value: String ->
            // Send an event to JavaScript.
            sendEvent(
                "onChange", mapOf(
                    "value" to value
                )
            )
        }

        //Define a function to get the battery percentage.
        Function("getBatteryLevel") {
            // Lấy context của ứng dụng React Native hiện tại
            val context = appContext.reactContext ?: return@Function -1

            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batteryLevel =
                batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            return@Function batteryLevel
        }
    }
}