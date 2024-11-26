package com.example.amoz.ui.states

import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.ProductDetails
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.view_models.ProductsViewModel
import kotlinx.coroutines.flow.MutableStateFlow

data class ProductsUiState(
    val productsListFetched: MutableStateFlow<ResultState<List<ProductSummary>>> = MutableStateFlow(ResultState.Idle),
    val productVariantsListFetched: MutableStateFlow<ResultState<List<ProductVariantSummary>>> = MutableStateFlow(ResultState.Idle),

    val productsList: List<ProductSummary> = emptyList(),
    val productVariantsList: List<ProductVariantSummary>? = null,
    val filteredSortedProductsList: List<ProductSummary> = productsList,
    val filteredSortedProductVariantsList: List<ProductVariantSummary>? = productVariantsList,

    val searchQuery: String = "",
    val filteredByProduct: ProductSummary? = null,
    val showProductsList: Boolean = true,
    val showProductVariantsList: Boolean = false,

    val filterParams: ProductsViewModel.FilterParams = ProductsViewModel.FilterParams(),
    val filterParamsInEdit: ProductsViewModel.FilterParams = ProductsViewModel.FilterParams(),

    val currentAddEditProductDetails: ProductDetails? = null,
    val currentAddEditProductState: MutableStateFlow<ResultState<ProductCreateRequest>> = MutableStateFlow(ResultState.Idle),

    val currentAddEditProductVariantDetails: ProductVariantDetails? = null,
    val currentAddEditProductVariantState: MutableStateFlow<ResultState<ProductVariantCreateRequest>> = MutableStateFlow(ResultState.Idle),

    val currentProductToDelete: ProductSummary? = null,
    val currentProductVariantToDelete: ProductVariantSummary? = null,


    val menuBottomSheetExpanded: Boolean = false,
    val addEditSimpleProductBottomSheetExpanded: Boolean = false,
    val addEditProductVariantBottomSheetExpanded: Boolean = false,
    val addEditProductBottomSheetExpanded: Boolean = false,

    val deleteProductBottomSheetExpanded: Boolean = false,

    val moreFiltersBottomSheetExpanded: Boolean = false,

    )
