package com.example.amoz.ui.screens.stock_update

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.amoz.models.ProductVariantDetails

@Composable
fun ProductVariantsStockList(
    productVariantsList: List<ProductVariantDetails>
) {
    LazyColumn {
        items(productVariantsList, key = { it.productVariantId }) {

        }
    }
}