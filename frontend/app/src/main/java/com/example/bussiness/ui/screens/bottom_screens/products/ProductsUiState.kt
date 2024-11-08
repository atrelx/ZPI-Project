package com.example.bussiness.ui.screens.bottom_screens.products

import com.example.bussiness.firebase.Product

data class ProductsUiState(
    val productsList: List<Product> = emptyList(),
    val menuBottomSheetExpanded: Boolean = false,
    val currentAddEditProduct: Product = Product(),
)
