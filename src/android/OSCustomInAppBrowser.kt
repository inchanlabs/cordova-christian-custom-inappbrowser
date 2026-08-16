package com.outsystems.plugins.custominappbrowser

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaPlugin
import org.json.JSONArray

class OSCustomInAppBrowser : CordovaPlugin() {

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

                    callbackContext.error(
                        "URL is required"
                    )

                    return true
                }

                val url = args.getString(0)

                cordova.activity.runOnUiThread {

                    val intent =
                        android.content.Intent(
                            cordova.activity,
                            BrowserActivity::class.java
                        )

                    intent.putExtra(
                        "url",
                        url
                    )

                    cordova.activity.startActivity(intent)
                }

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

    class BrowserActivity : Activity() {

        private lateinit var webView: WebView

        override fun onCreate(
            savedInstanceState: Bundle?
        ) {

            super.onCreate(savedInstanceState)

            androidx.core.view.WindowCompat.setDecorFitsSystemWindows(
            window,
            true
            )

            // Hide the Android action bar.
            actionBar?.hide()

            // Keep the phone's status bar visible.
            window.clearFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            val url =
                intent.getStringExtra("url") ?: ""

            val root =
                LinearLayout(this)

            root.orientation =
                LinearLayout.VERTICAL

            webView =
                WebView(this)

            webView.settings.javaScriptEnabled =
                true

            webView.settings.domStorageEnabled =
                true

            webView.webViewClient =
                WebViewClient()

            webView.webChromeClient =
                WebChromeClient()

            root.addView(
                webView,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            setContentView(root)

            webView.loadUrl(url)
        }

        override fun onBackPressed() {

            if (webView.canGoBack()) {

                webView.goBack()

            } else {

                super.onBackPressed()

            }
        }

        override fun onDestroy() {

            webView.stopLoading()
            webView.destroy()

            super.onDestroy()
        }
    }
}
