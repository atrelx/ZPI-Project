package com.example.amoz.ui.states

import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.CategorySummary
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.view_models.ProductsViewModel.SortingType
import com.example.amoz.test_data.products.summary.testProductSummaryList
import com.example.amoz.test_data.products.summary.testProductVariantSummariesList
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.BigDecimal
import java.util.UUID

data class ProductsUiState(
    val productsListFetched: MutableStateFlow<ResultState<List<ProductSummary>>> = MutableStateFlow(ResultState.Idle),
    val productVariantsListFetched: MutableStateFlow<ResultState<List<ProductVariantSummary>>> = MutableStateFlow(ResultState.Idle),

    val productsList: List<ProductSummary> = emptyList(),
    val productVariantsList: List<ProductVariantSummary> = emptyList(),
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

    val currentAddEditProductId: UUID? = null,
    val currentAddEditProductState: MutableStateFlow<ResultState<ProductCreateRequest>> = MutableStateFlow(ResultState.Idle),
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
