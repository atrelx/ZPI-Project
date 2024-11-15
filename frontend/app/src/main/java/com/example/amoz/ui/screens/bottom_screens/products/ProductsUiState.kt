package com.example.amoz.ui.screens.bottom_screens.products

import com.example.amoz.data.ProductTemplate
import com.example.amoz.data.ProductVariant
import com.example.amoz.ui.screens.bottom_screens.products.ProductsViewModel.SortingType
import com.example.amoz.ui.screens.bottom_screens.products.products_list.testProductTemplatesList
import com.example.amoz.ui.screens.bottom_screens.products.products_list.testProductVariantsList

data class ProductsUiState(
    val productTemplatesList: List<ProductTemplate> = testProductTemplatesList,
    val productVariantsList: List<ProductVariant> = testProductVariantsList,
    val filteredSortedProductTemplatesList: List<ProductTemplate> = productTemplatesList,
    val filteredSortedProductVariantsList: List<ProductVariant> = productVariantsList,

    val searchQuery: String = "",
    val selectedProductTemplate: ProductTemplate? = null,
    val showProductTemplatesList: Boolean = true,
    val showProductVariantsList: Boolean = false,

    val sortingType: SortingType = SortingType.NONE,
    val filterPriceFrom: Double = 0.0,
    val filterPriceTo: Double = Int.MAX_VALUE.toDouble(),
    val filterCategory: String = "",

    val currentAddEditProduct: ProductTemplate = ProductTemplate(),

    val menuBottomSheetExpanded: Boolean = false,
    val addEditSimpleProductBottomSheetExpanded: Boolean = false,
    val addEditProductVariantBottomSheetExpanded: Boolean = false,
    val addEditProductTemplateBottomSheetExpanded: Boolean = false,

    val deleteProductTemplateBottomSheetExpanded: Boolean = false,

    val moreFiltersBottomSheetExpanded: Boolean = false,

    )
