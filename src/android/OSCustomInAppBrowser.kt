package com.outsystems.plugins.custominappbrowser

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
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

        private lateinit var reloadButton: Button
        private lateinit var closeButton: Button

        override fun onCreate(
            savedInstanceState: Bundle?
        ) {

            super.onCreate(savedInstanceState)

            // Hide Android action bar.
            actionBar?.hide()

            val url =
                intent.getStringExtra("url") ?: ""

            /*
             * Root container
             */
            val root =
                LinearLayout(this)

            root.orientation =
                LinearLayout.VERTICAL

            /*
             * Respect Android system bars.
             */
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

            /*
             * Toolbar
             */
            val toolbar =
                LinearLayout(this)

            toolbar.orientation =
                LinearLayout.HORIZONTAL

            toolbar.gravity =
                Gravity.CENTER_VERTICAL

            toolbar.setPadding(
                8,
                4,
                8,
                4
            )

            /*
             * Reload
             */
            reloadButton =
                createButton("↻")

            reloadButton.setOnClickListener {

                webView.reload()

            }

            toolbar.addView(
                reloadButton
            )

            /*
             * Spacer
             *
             * Pushes Close to the
             * right side.
             */
            val spacer =
                View(this)

            toolbar.addView(
                spacer,
                LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1f
                )
            )

            /*
             * Close
             */
            closeButton =
                createButton("✕")

            closeButton.setOnClickListener {

                finish()

            }

            toolbar.addView(
                closeButton
            )

            root.addView(
                toolbar,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )

            /*
             * WebView
             */
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
                    0,
                    1f
                )
            )

            setContentView(root)

            /*
             * Apply status bar/navigation bar
             * insets.
             */
            ViewCompat.requestApplyInsets(root)

            /*
             * Load contract URL.
             */
            webView.loadUrl(url)
        }

        private fun createButton(
            text: String
        ): Button {

            val button =
                Button(this)

            button.text =
                text

            button.textSize =
                22f

            button.typeface =
                Typeface.DEFAULT_BOLD

            button.setTextColor(
                Color.BLACK
            )

            button.minWidth =
                56

            button.minHeight =
                48

            button.gravity =
                Gravity.CENTER

            return button
        }

        override fun onBackPressed() {

            /*
             * Since this is a contract viewer,
             * Android Back closes the viewer
             * instead of navigating the website.
             */
            finish()
        }

        override fun onDestroy() {

            webView.stopLoading()

            webView.destroy()

            super.onDestroy()
        }
    }
}
