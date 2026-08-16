package com.outsystems.plugins.custominappbrowser

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

            actionBar?.hide()

            val url =
                intent.getStringExtra("url") ?: ""

            val root =
                LinearLayout(this)

            root.orientation =
                LinearLayout.VERTICAL

            ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->

                val systemBars =
                    insets.getInsets(
                        WindowInsetsCompat.Type.systemBars()
                    )

                view.setPadding(
                    0,
                    systemBars.top,
                    0,
                    systemBars.bottom
                )

                insets
            }

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

            ViewCompat.requestApplyInsets(root)

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
