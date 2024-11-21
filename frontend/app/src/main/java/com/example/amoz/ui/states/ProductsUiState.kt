package com.example.amoz.ui.states

import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.models.CategorySummary
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.view_models.ProductsViewModel.SortingType
import com.example.amoz.test_data.products.summary.testProductSummaryList
import com.example.amoz.test_data.products.summary.testProductVariantSummariesList
import java.math.BigDecimal

data class ProductsUiState(
    val productsList: List<ProductSummary> = testProductSummaryList,
    val productVariantsList: List<ProductVariantSummary> = testProductVariantSummariesList,
    val filteredSortedProductTemplatesList: List<ProductSummary> = productsList,
    val filteredSortedProductVariantsList: List<ProductVariantSummary> = productVariantsList,

    val searchQuery: String = "",
    val filteredByProduct: ProductSummary? = null,
    val showProductsList: Boolean = true,
    val showProductVariantsList: Boolean = false,

    val sortingType: SortingType = SortingType.NONE,
    val filterPriceFrom: BigDecimal = BigDecimal.ZERO,
    val filterPriceTo: BigDecimal? = null,
    val filterCategory: CategorySummary? = null,

    val currentAddEditProduct: ProductCreateRequest? = null,
    val currentAddEditProductVariant: ProductVariantCreateRequest? = null,

    val currentProductToDelete: ProductSummary? = null,
    val currentProductVariantToDelete: ProductVariantSummary? = null,


    val menuBottomSheetExpanded: Boolean = false,
    val addEditSimpleProductBottomSheetExpanded: Boolean = false,
    val addEditProductVariantBottomSheetExpanded: Boolean = false,
    val addEditProductBottomSheetExpanded: Boolean = false,

    val deleteProductBottomSheetExpanded: Boolean = false,

    val moreFiltersBottomSheetExpanded: Boolean = false,

    )
