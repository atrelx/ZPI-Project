package com.example.amoz.ui.screens.bottom_screens.products.products_list.list_items

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.amoz.models.ProductDetails
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.models.ProductVariantSummary

@Composable
fun ProductVariantListItem(
    productVariant: ProductVariantSummary,
    onClick: () -> Unit,
    currency: String
) {
    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = onClick,
            ),
//        leadingContent = {
//            productVariant.image?.let {
//                Image(
//                    bitmap = it.asImageBitmap(),
//                    contentDescription = null,
//                    modifier = Modifier.size(40.dp),
//                    contentScale = ContentScale.Crop
//                )
//            } ?: run {
//                Icon(
//                    imageVector = Icons.Default.Inventory,
//                    contentDescription = "product image"
//                )
//            }
//        },
        headlineContent = {
            Text(
                text = productVariant.variantName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(text = "Category: ${productVariant.code}")
        },
        trailingContent = {
            Text(
                text = "${productVariant.variantPrice} $currency",
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
    )
}