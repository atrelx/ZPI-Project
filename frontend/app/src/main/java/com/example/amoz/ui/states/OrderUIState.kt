package com.example.amoz.ui.states

import com.example.amoz.data.ProductVariant
import com.example.amoz.firebase.SoldProduct

data class OrderUIState (
    val salesList: List<SoldProduct> = emptyList(),
    val productsList: List<ProductVariant> = emptyList(),
    val salesListIsLoading: Boolean = true,
    val showAddEditSaleDialog: Boolean = false,
    val showFilterBottomSheet: Boolean = false,
    val currentAddEditSaleProduct: SoldProduct = SoldProduct()
)