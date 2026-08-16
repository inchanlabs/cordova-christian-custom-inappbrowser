package com.outsystems.plugins.custominappbrowser

import android.util.Log
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaPlugin
import org.json.JSONArray

class OSCustomInAppBrowser : CordovaPlugin() {

    companion object {
        private const val TAG = "OSCustomInAppBrowser"
    }

    override fun pluginInitialize() {
        super.pluginInitialize()

        Log.d(TAG, "Custom InAppBrowser plugin initialized")
    }

    override fun execute(
        action: String,
        args: JSONArray,
        callbackContext: CallbackContext
    ): Boolean {

        when (action) {

            "isAvailable" -> {

                callbackContext.success(1)

                return true
            }

            "open" -> {

                if (args.length() == 0) {
                    callbackContext.error("URL is required")
                    return true
                }

                val url = args.getString(0)

                Log.d(TAG, "Opening URL: $url")

                callbackContext.success()

                return true
            }

            else -> {

                callbackContext.error(
                    "Unknown action: $action"
                )

                return false
            }
        }
    }
}
