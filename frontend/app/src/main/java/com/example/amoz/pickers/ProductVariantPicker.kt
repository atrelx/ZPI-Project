package com.example.amoz.pickers

import androidx.navigation.NavController
import com.example.amoz.data.SavedStateHandleKeys
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.ui.screens.Screens

class ProductVariantPicker(navController: NavController)
    : BasePicker<ProductVariantDetails>(navController, ProductVariantDetails.serializer()) {

    fun isProductVariantPickerMode(): Boolean {
       return previousSavedStateHandle?.get<Boolean>(
           SavedStateHandleKeys.PRODUCT_VARIANT_PICKER_MODE
       ) ?: false
    }

    fun getPickedProductVariant(): ProductVariantDetails? {
        return currentSavedStateHandle?.get<String>(
            SavedStateHandleKeys.PICKED_PRODUCT_VARIANT_DETAILS
        )?.let { decodeFromJson(it) }
    }

    fun navigateToProductScreen() {
        setNavElementsVisibleMode(false)
        setProductVariantPickerMode(true)
        navController.navigate(Screens.Products.route)
    }

    fun pickProductVariant(productVariantDetails: ProductVariantDetails) {
        previousSavedStateHandle?.set(
            SavedStateHandleKeys.PICKED_PRODUCT_VARIANT_DETAILS,
            encodeToJson(productVariantDetails)
        )
        navController.popBackStack()
        setProductVariantPickerMode(mode = false)
        setNavElementsVisibleMode(mode = true)
    }

    private fun setProductVariantPickerMode(mode: Boolean) {
        setMode(SavedStateHandleKeys.PRODUCT_VARIANT_PICKER_MODE, mode)
    }
}