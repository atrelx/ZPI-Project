package com.example.amoz.ui.screens.stock_update

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.ui.components.EmptyLayout
import com.example.amoz.ui.components.PrimaryFilledButton

@Composable
fun EmptyStockLayout(
    onAdd: () -> Unit,
) {
    EmptyLayout {
        Text(
            text = stringResource(R.string.empty_list_greeting),
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(10.dp))
        PrimaryFilledButton(
            onClick = onAdd,
            text = stringResource(R.string.add_product_variant),
            leadingIcon = Icons.Default.Add
        )
    }
}