package com.example.amoz.ui.screens.bottom_screens.products

import com.example.amoz.data.ProductTemplate
import com.example.amoz.ui.screens.bottom_screens.products.lazy_products_list.testProductTemplatesList

data class ProductsUiState(
    val productTemplatesList: List<ProductTemplate> = testProductTemplatesList,
    val menuBottomSheetExpanded: Boolean = false,
    val addEditProductBottomSheetExpanded: Boolean = false,
    val currentAddEditProduct: ProductTemplate = ProductTemplate(),
)
