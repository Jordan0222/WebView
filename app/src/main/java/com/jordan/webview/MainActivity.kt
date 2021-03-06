package com.jordan.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jordan.webview.ui.theme.WebViewTheme
import dagger.hilt.android.AndroidEntryPoint

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
            if (state.webViewProgress != 100) {
                LinearProgressIndicator(
                    progress = state.webViewProgress * 1.0F / 100F,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp),
                    color = Color.Green
                )
            }
            CustomWebView(
                modifier = Modifier.fillMaxSize(),
                url = url,
                initSettings = { webView ->
                    webView.settings.apply {
                        //?????? js ??????
                        javaScriptEnabled = true
                        //???????????????????????? webView ?????????
                        useWideViewPort = true
                        // ????????????????????????
                        loadWithOverviewMode = true
                        // ????????????
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = true
                        // ?????????????????? JS ???????????????
                        javaScriptCanOpenWindowsAutomatically = true
                        // ?????????????????????
                        cacheMode = WebSettings.LOAD_NO_CACHE
                        loadWithOverviewMode = true
                    }
                },
                onProgressChange = { progress ->
                    viewModel.onProgressChange(progress)
                }
            )
        }
    }
}