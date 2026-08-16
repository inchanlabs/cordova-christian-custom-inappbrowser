package com.outsystems.plugins.custominappbrowser

import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaPlugin
import org.json.JSONArray

class OSCustomInAppBrowser : CordovaPlugin() {

    override fun execute(
        action: String,
        args: JSONArray,
        callbackContext: CallbackContext
    ): Boolean {

        if (action == "isAvailable") {

            callbackContext.success(1)

            return true
        }

        callbackContext.error(
            "Unknown action: $action"
        )

        return false
    }
}
