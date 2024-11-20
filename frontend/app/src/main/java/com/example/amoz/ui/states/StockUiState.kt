package com.example.amoz.ui.states

import com.example.amoz.models.ProductVariantDetails

data class StockUiState (
    val productVariants: List<ProductVariantDetails> = emptyList(),

    )