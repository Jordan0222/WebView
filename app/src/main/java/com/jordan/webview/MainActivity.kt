package com.jordan.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.jordan.webview.ui.theme.WebViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WebViewScreen()
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val url = viewModel.urlState.value

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomWebView(
                modifier = Modifier.fillMaxSize(),
                url = url,
                initSettings = { webView ->
                    webView.settings.apply {
                        //支持 js 交互
                        javaScriptEnabled = true
                        //將圖片調整到適合 webView 的大小
                        useWideViewPort = true
                        // 縮放至屏幕的大小
                        loadWithOverviewMode = true
                        // 縮放操作
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = true
                        // 是否支持通過 JS 打開新窗口
                        javaScriptCanOpenWindowsAutomatically = true
                        // 不加載缓存内容
                        cacheMode = WebSettings.LOAD_NO_CACHE
                    }
                },
                onProgressChange = { progress ->
                    viewModel.onProgressChange(progress)
                }
            )
        }
        if (state.webViewProgress != 100) {
            LinearProgressIndicator(
                progress = state.webViewProgress * 1.0F / 100F,
                modifier = Modifier
                    .width(150.dp)
                    .height(15.dp)
                    .align(Alignment.Center),
                color = Color.Green
            )
        }
    }
}