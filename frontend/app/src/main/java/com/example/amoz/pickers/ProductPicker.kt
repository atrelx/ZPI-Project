package com.example.amoz.pickers

import androidx.navigation.NavController
import com.example.amoz.data.SavedStateHandleKeys
import com.example.amoz.models.ProductSummary
import com.example.amoz.ui.screens.Screens

class ProductPicker(navController: NavController)
    : BasePicker<ProductSummary>(navController, ProductSummary.serializer()) {

    fun isProductPickerMode(): Boolean {
        return previousSavedStateHandle?.get<Boolean>(
            SavedStateHandleKeys.PRODUCT_PICKER_MODE
        ) ?: false
    }

    fun getPickedProduct(): ProductSummary? {
        return currentSavedStateHandle?.get<String>(
            SavedStateHandleKeys.PICKED_PRODUCT_SUMMARY
        )?.let { decodeFromJson(it) }
    }

    fun navigateToProductScreen() {
        setNavElementsVisibleMode(false)
        setProductPickerMode(true)
        navController.navigate(Screens.Products.route)
    }

    fun pickProduct(productSummary: ProductSummary) {
        previousSavedStateHandle?.set(
            SavedStateHandleKeys.PICKED_PRODUCT_SUMMARY,
            encodeToJson(productSummary)
        )
        navController.popBackStack()
        setProductPickerMode(mode = false)
        setNavElementsVisibleMode(mode = true)
    }

    private fun setProductPickerMode(mode: Boolean) {
        setMode(SavedStateHandleKeys.PRODUCT_PICKER_MODE, mode)
    }
}