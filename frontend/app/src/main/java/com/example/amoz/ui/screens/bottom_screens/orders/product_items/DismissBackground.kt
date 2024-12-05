package com.example.amoz.ui.screens.bottom_screens.orders.product_items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.amoz.ui.theme.customColor2Dark
import com.example.amoz.ui.theme.onCustomColor2Dark

@Composable
fun DismissBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(customColor2Dark.value))
            .padding(12.dp, 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            tint = Color(onCustomColor2Dark.value),
            contentDescription = "Remove product"
        )
    }
}
