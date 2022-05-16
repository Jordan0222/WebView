package com.jordan.webview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewState
import java.lang.Exception

@Composable
fun CustomWebView(
    modifier: Modifier = Modifier,
    url: String,
    onProgressChange: (progress: Int) -> Unit = {},
    initSettings: (webView: WebView) -> Unit = {},
    onReceivedError: (webResourceError: WebResourceError) -> Unit = {}
) {
    val urlState = rememberWebViewState(url = url)

    val webChromeClient = remember {
        object : AccompanistWebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                onProgressChange(newProgress)
                super.onProgressChanged(view, newProgress)
            }
        }
    }

    val webClient = remember {
        object : AccompanistWebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                onProgressChange(100)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                onProgressChange(-1)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (request?.url == null) {
                    return false
                }
                val showOverrideUrl = request.url.toString()
                try {
                    if (!showOverrideUrl.startsWith("http://")
                        && !showOverrideUrl.startsWith("https://")) {
                        Intent(Intent.ACTION_VIEW, Uri.parse(showOverrideUrl)).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            view?.context?.applicationContext?.startActivity(this)
                        }
                        return true
                    }
                } catch (e: Exception) {
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                error?.let { onReceivedError(it) }
            }
        }
    }

    WebView(
        state = urlState,
        modifier = modifier.fillMaxSize(),
        client = webClient,
        chromeClient = webChromeClient,
        onCreated = { webView ->
            initSettings(webView)
        }
    )
}