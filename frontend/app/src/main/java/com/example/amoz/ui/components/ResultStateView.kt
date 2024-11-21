package com.example.amoz.ui.commonly_used_components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.amoz.api.sealed.ResultState
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun <T> ResultStateView(state: MutableStateFlow<ResultState<T>>,
                        modifier: Modifier = Modifier,
                        loadingView: (@Composable () -> Unit)? = null,
                        failureView: @Composable ((String) -> Unit)? = null,
                        successView: @Composable (T) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (val resultState = state.collectAsState().value) {
            is ResultState.Idle -> {
                Unit
            }

            is ResultState.Loading -> {
                loadingView ?: LoadingView()
            }

            is ResultState.Failure -> {
                if (failureView != null) {
                    failureView.invoke(resultState.message)
                } else {
                    LaunchedEffect(resultState.message) {
                        Toast.makeText(context, resultState.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            is ResultState.Success -> {
                successView(resultState.data)
            }
        }
    }
}
