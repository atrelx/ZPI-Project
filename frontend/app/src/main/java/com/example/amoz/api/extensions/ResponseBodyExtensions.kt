package com.example.amoz.api.extensions

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import okhttp3.ResponseBody

fun ResponseBody?.toImageBitmap(): ImageBitmap? {
    return this?.byteStream()?.use { inputStream ->
        val byteArray = inputStream.readBytes()
        byteArray.toBitmap().asImageBitmap()
    }
}