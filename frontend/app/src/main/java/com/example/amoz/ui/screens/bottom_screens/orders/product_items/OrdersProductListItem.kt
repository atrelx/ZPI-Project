package com.example.amoz.ui.screens.bottom_screens.orders.product_items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.view_models.OrdersViewModel.ProductVariantOrderItem

@Composable
fun ProductListItem(
    product: ProductVariantOrderItem,
    onClick: () -> Unit,
    onProductRemove: (ProductVariantOrderItem) -> Unit,
    currency: String,
    positionalThreshold: Float = .45f
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.remove_product_from_order_title)) },
            text = { Text(stringResource(R.string.remove_product_from_order_text)) },
            confirmButton = {
                Button(
                    onClick = {
                        onProductRemove(product)
                        showDialog = false
                    }
                ) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }

    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = { showDialog = true }),
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.FilterList,
                contentDescription = null
            )
        },
        headlineContent = {
            Text(
                text = "${product.productVariant.variantName} x${product.quantity}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Text(
                text = "${product.totalPrice} $currency"
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
    )
}
