package com.example.amoz.extensions

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.ui.commonly_used_components.LoadingView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun <T> MutableStateFlow<ResultState<T>>.updateResultState(function: (T) -> T)  {
    this.fetchDataIfSuccess()?.let {
        val updatedData = function(it)
        this.value = ResultState.Success(updatedData)
    }
}

fun <T> MutableStateFlow<ResultState<T>>.fetchDataIfSuccess(): T? {
    val state = this.value
    return if (state is ResultState.Success) {
        state.data
    } else {
        null
    }
}

