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

        Log.d(TAG, "Plugin initialized")
    }

    override fun execute(
        action: String,
        args: JSONArray,
        callbackContext: CallbackContext
    ): Boolean {

        Log.d(TAG, "Received action: $action")

        callbackContext.success(
            "ACTION_RECEIVED:$action"
        )

        return true
    }
}
