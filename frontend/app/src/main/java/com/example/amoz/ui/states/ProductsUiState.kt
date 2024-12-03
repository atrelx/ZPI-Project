package com.example.amoz.ui.states

import androidx.compose.ui.graphics.ImageBitmap
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.ProductDetails
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.view_models.ProductsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

data class ProductsUiState(
    val productsListFetched: MutableStateFlow<ResultState<List<ProductSummary>>> = MutableStateFlow(ResultState.Idle),
    val productVariantsListFetched: MutableStateFlow<ResultState<List<ProductVariantSummary>>> = MutableStateFlow(ResultState.Idle),
    val productVariantImages: MutableStateFlow<MutableMap<UUID, MutableStateFlow<ResultState<ImageBitmap?>>>> = MutableStateFlow(
        HashMap()
    ),
    val productsList: List<ProductSummary> = emptyList(),
    val productVariantsList: List<ProductVariantSummary> = emptyList(),
    val filteredSortedProductsList: List<ProductSummary> = productsList,
    val filteredSortedProductVariantsList: List<ProductVariantSummary> = productVariantsList,

    val searchQuery: String = "",
    val filteredByProduct: ProductSummary? = null,
    val showProductsList: Boolean = true,
    val showProductVariantsList: Boolean = false,

    val filterParams: ProductsViewModel.FilterParams = ProductsViewModel.FilterParams(),
    val filterParamsInEdit: ProductsViewModel.FilterParams = ProductsViewModel.FilterParams(),

    val currentAddEditProductCreateRequest: ProductCreateRequest = ProductCreateRequest(),
    val currentAddEditProductDetailsState: MutableStateFlow<ResultState<ProductDetails?>> = MutableStateFlow(ResultState.Idle),

    val currentAddEditProductVariantCreateRequest: ProductVariantCreateRequest = ProductVariantCreateRequest(),
    val currentAddEditProductVariantDetailsState: MutableStateFlow<ResultState<ProductVariantDetails?>> = MutableStateFlow(ResultState.Idle),

    val currentAddEditSimpleProduct: Pair<ProductCreateRequest, ProductVariantCreateRequest> = Pair(ProductCreateRequest(), ProductVariantCreateRequest()),

    val currentProductToDelete: ProductSummary? = null,
    val currentProductVariantToDelete: ProductVariantSummary? = null,


    val menuBottomSheetExpanded: Boolean = false,
    val addEditSimpleProductBottomSheetExpanded: Boolean = false,
    val addEditProductVariantBottomSheetExpanded: Boolean = false,
    val addEditProductBottomSheetExpanded: Boolean = false,

    val deleteProductBottomSheetExpanded: Boolean = false,

    val moreFiltersBottomSheetExpanded: Boolean = false,

    )
