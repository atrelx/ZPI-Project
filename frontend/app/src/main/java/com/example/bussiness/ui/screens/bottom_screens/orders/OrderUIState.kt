package com.example.bussiness.ui.screens.bottom_screens.orders

import com.example.bussiness.firebase.Product
import com.example.bussiness.firebase.SoldProduct

data class OrderUIState (
    val salesList: List<SoldProduct> = emptyList(),
    val productsList: List<Product> = emptyList(),
    val salesListIsLoading: Boolean = true,
    val showAddEditSaleDialog: Boolean = false,
    val showFilterBottomSheet: Boolean = false,
    val currentAddEditSaleProduct: SoldProduct = SoldProduct()
)