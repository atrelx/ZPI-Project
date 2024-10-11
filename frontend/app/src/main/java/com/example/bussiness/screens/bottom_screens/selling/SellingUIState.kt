package com.example.bussiness.screens.bottom_screens.selling

import com.example.bussiness.firebase.Product
import com.example.bussiness.firebase.SoldProduct

data class SellingUIState (
    val salesList: List<SoldProduct> = emptyList(),
    val productsList: List<Product> = emptyList(),
    val salesListIsLoading: Boolean = true,
    val showAddEditSaleDialog: Boolean = false,
    val showFilterBottomSheet: Boolean = false,
    val currentAddEditSaleProduct: SoldProduct = SoldProduct()
)