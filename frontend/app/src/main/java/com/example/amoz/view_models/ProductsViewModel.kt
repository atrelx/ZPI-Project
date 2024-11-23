package com.example.amoz.view_models

import androidx.lifecycle.ViewModel
import com.example.amoz.api.requests.AttributeCreateRequest
import com.example.amoz.api.requests.DimensionsCreateRequest
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.requests.StockCreateRequest
import com.example.amoz.api.requests.WeightCreateRequest
import com.example.amoz.models.CategorySummary
import com.example.amoz.models.ProductDetails
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.test_data.products.details.testProductDetailsList
import com.example.amoz.test_data.products.details.testProductVariantDetailsList
import com.example.amoz.ui.states.ProductsUiState
import com.example.amoz.ui.screens.bottom_screens.products.products_list.ProductListFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.util.UUID

class ProductsViewModel : ViewModel() {
    private val _productUiState = MutableStateFlow(ProductsUiState())
    val productUiState: StateFlow<ProductsUiState> = _productUiState.asStateFlow()
    private val productFilter = ProductListFilter()

    fun getProductDetails(productId: UUID?): ProductDetails? {
        /*TODO*/
        return productId?.let { testProductDetailsList.find { it.productId == productId } }
    }

    fun getProductVariantDetails(productVariantId: UUID?): ProductVariantDetails? {
        /*TODO*/
        return productVariantId?.let { testProductVariantDetailsList.find { it.productVariantId == productVariantId } }
    }

    fun updateCurrentAddEditProduct(productId: UUID?) {
        val product: ProductDetails? = getProductDetails(productId)
        val addEditProduct = ProductCreateRequest(
            name = product?.name ?: "",
            price = product?.price ?: BigDecimal.ZERO,
            categoryId = product?.category?.categoryId ?: UUID.randomUUID() /*TODO*/,
            description = product?.description,
            brand = product?.brand,
            productVariantIds = emptyList(),
            productAttributes = product?.productAttributes?.map {
                AttributeCreateRequest(
                    attributeName = it.attribute.attributeName,
                    value = it.value
                )
            } ?: emptyList()
        )
        _productUiState.update { it.copy(currentAddEditProduct = addEditProduct) }
    }

    fun updateCurrentAddEditProductVariant(productId: UUID?, productVariantId: UUID?) {
        val productVariant = getProductVariantDetails(productVariantId)
        val addEditProduct = ProductVariantCreateRequest(
            productID = productId ?: _productUiState.value.productsList.first().productId,
            productVariantCode = productVariant?.code ?: 0,
            stock = StockCreateRequest(
                amountAvailable = productVariant?.stock?.amountAvailable ?: 0,
                alarmingAmount = productVariant?.stock?.alarmingAmount ?: 5
            ),
            weight = productVariant?.weight?.let {
                WeightCreateRequest(
                    unitWeight = it.unitWeight,
                    amount = it.amount
                )
            } ,
            dimensions = productVariant?.dimensions?.let {
                DimensionsCreateRequest(
                    unitDimensions = it.unitDimensions,
                    height = it.height,
                    length = it.length,
                    width = it.width
                )
            },
            variantAttributes = productVariant?.variantAttributes?.map {
                AttributeCreateRequest(
                    attributeName = it.attribute.attributeName,
                    value = it.value
                )
            } ?: emptyList(),
            variantName = productVariant?.variantName,
            variantPrice = productVariant?.variantPrice ?: BigDecimal.ZERO
        )
        _productUiState.update { it.copy(currentAddEditProductVariant = addEditProduct) }
    }

    fun updateCurrentProductToDelete(productSummary: ProductSummary?) {
        _productUiState.update { it.copy(currentProductToDelete = productSummary) }
    }

    fun updateCurrentProductVariantToDelete(productVariantSummary: ProductVariantSummary?) {
        _productUiState.update { it.copy(currentProductVariantToDelete = productVariantSummary) }
    }

    fun removeProductFromList(product: ProductSummary) {
        /*TODO*/
        _productUiState.update { currState ->
            val productIndexToDelete = currState.productsList.indexOf(currState.productsList.find { it.productId == product.productId })
            val productIndexToDeleteFiltered = currState.filteredSortedProductTemplatesList.indexOf(currState.filteredSortedProductTemplatesList.find { it.productId == product.productId })
            currState.copy(
                productsList = currState.productsList.toMutableList().apply {
                    removeAt(productIndexToDelete)
                },
                filteredSortedProductTemplatesList = currState.filteredSortedProductTemplatesList.toMutableList().apply {
                    removeAt(productIndexToDeleteFiltered)
                }
            )
        }
    }

    // Helper method for updating state with filters applied
    private fun applyFilters() {
        _productUiState.update { currState ->
            currState.copy(
                filteredSortedProductTemplatesList = if (currState.showProductsList) {
                    productFilter.filterProductTemplates(
                        templates = currState.productsList,
                        searchQuery = currState.searchQuery,
                        sortingType = currState.sortingType,
                        priceFrom = currState.filterPriceFrom,
                        priceTo = currState.filterPriceTo,
                        category = currState.filterCategory
                    )
                } else currState.filteredSortedProductTemplatesList
                ,
                filteredSortedProductVariantsList = if (currState.showProductVariantsList) {
                    productFilter.filterProductVariants(
                        variants = currState.productVariantsList,
                        searchQuery = currState.searchQuery,
                        selectedTemplate = currState.filteredByProduct,
                        sortingType = currState.sortingType,
                        priceFrom = currState.filterPriceFrom,
                        priceTo = currState.filterPriceTo
                    )
                } else currState.filteredSortedProductVariantsList
            )
        }
    }

    fun cancelFilters() {
        _productUiState.update { currState ->
            currState.copy(
                sortingType = SortingType.NONE,
                filterPriceFrom = BigDecimal.ZERO,
                filterPriceTo = null,
                filterCategory = null,
            )
        }
        applyFilters()
    }

    fun updateSearchQuery(query: String) {
        _productUiState.update { currState ->
            currState.copy(
                searchQuery = query,
                showProductVariantsList = query.isNotBlank()
            )
        }
        applyFilters()
    }

    fun updateFilteredByProduct(selectedProductTemplate: ProductSummary?) {
        _productUiState.update { currState ->
            currState.copy(
                showProductsList = selectedProductTemplate == null,
                showProductVariantsList = selectedProductTemplate != null,
                filteredByProduct = selectedProductTemplate
            )
        }
        applyFilters()
    }

    // Update filter state with one function call, reducing the need for multiple function parameters
    fun updateFilters(filterParams: FilterParams) {
        _productUiState.update { currState ->
            currState.copy(
                sortingType = filterParams.ascendingSorting,
                filterPriceFrom = filterParams.priceFrom,
                filterPriceTo = filterParams.priceTo,
                filterCategory = filterParams.category
            )
        }
        applyFilters()
    }

    fun clearPriceFilter(from: Boolean = true) {
        _productUiState.update { currState ->
            currState.copy(
                filterPriceFrom = if (from) BigDecimal.ZERO else currState.filterPriceFrom,
                filterPriceTo = if (!from) null else currState.filterPriceTo
            )
        }
        applyFilters()
    }

    // Reusable function to expand any bottom sheet
    fun expandBottomSheet(type: BottomSheetType, expand: Boolean) {
        _productUiState.update { currState ->
            currState.copy(
                menuBottomSheetExpanded = if (type == BottomSheetType.MENU) expand else currState.menuBottomSheetExpanded,
                addEditSimpleProductBottomSheetExpanded = if (type == BottomSheetType.ADD_EDIT_SIMPLE) expand else currState.addEditSimpleProductBottomSheetExpanded,
                addEditProductVariantBottomSheetExpanded = if (type == BottomSheetType.ADD_EDIT_VARIANT) expand else currState.addEditProductVariantBottomSheetExpanded,
                addEditProductBottomSheetExpanded = if (type == BottomSheetType.ADD_EDIT_PRODUCT) expand else currState.addEditProductBottomSheetExpanded,
                moreFiltersBottomSheetExpanded = if (type == BottomSheetType.MORE_FILTERS) expand else currState.moreFiltersBottomSheetExpanded,
                deleteProductBottomSheetExpanded = if (type == BottomSheetType.DELETE_PRODUCT) expand else currState.deleteProductBottomSheetExpanded
            )
        }
    }

    enum class BottomSheetType {
        MENU, ADD_EDIT_SIMPLE, ADD_EDIT_VARIANT, ADD_EDIT_PRODUCT, MORE_FILTERS, DELETE_PRODUCT
    }

    enum class SortingType {
        NONE, ASCENDING_NAME, DESCENDING_NAME, ASCENDING_PRICE, DESCENDING_PRICE
    }

    data class FilterParams(
        val ascendingSorting: SortingType,
        val priceFrom: BigDecimal,
        val priceTo: BigDecimal?,
        val category: CategorySummary?
    )
}


