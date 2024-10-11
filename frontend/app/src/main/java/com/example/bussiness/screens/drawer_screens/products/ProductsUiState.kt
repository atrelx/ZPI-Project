package com.example.bussiness.screens.drawer_screens.products

import com.example.bussiness.firebase.Product

data class ProductsUiState(
    val productsList: List<Product> = emptyList(),
    val showProductAddEditView: Boolean = false,
    val currentAddEditProduct: Product = Product(),
)
