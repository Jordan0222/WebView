package com.jordan.webview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _state = mutableStateOf(WebViewState())
    val state: State<WebViewState> = _state

    private val _urlState = mutableStateOf("https://google.com")
    val urlState: State<String> = _urlState

    fun onProgressChange(progress: Int) {
        _state.value = state.value.copy(
            webViewProgress = progress
        )
    }
}