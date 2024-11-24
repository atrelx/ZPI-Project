package com.example.amoz.view_models

import androidx.lifecycle.viewModelScope
import com.example.amoz.api.repositories.ProductRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.api.requests.AttributeCreateRequest
import com.example.amoz.api.requests.DimensionsCreateRequest
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.requests.StockCreateRequest
import com.example.amoz.api.requests.WeightCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.CategoryTree
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.test_data.products.details.testProductVariantDetailsList
import com.example.amoz.ui.states.ProductsUiState
import com.example.amoz.ui.screens.bottom_screens.products.products_list.ProductListFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val productVariantRepository: ProductVariantRepository,
) : BaseViewModel() {
    private val _productUiState = MutableStateFlow(ProductsUiState())
    val productUiState: StateFlow<ProductsUiState> = _productUiState.asStateFlow()
    private val productFilter = ProductListFilter()

    init {
        fetchProductsList()
        viewModelScope.launch {
            _productUiState
                .map { it.filteredByProduct }
                .distinctUntilChanged()
                .collect { filteredProduct ->
                    filteredProduct?.let { fetchProductVariantsList(it.productId) }
                }
        }
    }

//    fun fetchCategoryDetails(categoryId: UUID) {
//        performRepositoryAction(
//            binding = null,
//            action = { categoryRepository. }
//        )
//    }


    fun fetchProductsList(skipLoading: Boolean = false) {
        performRepositoryAction(
            binding = _productUiState.value.productsListFetched,
            failureMessage = "Could not fetch products, try again",
            skipLoading = skipLoading,
            action = { productRepository.getAllProducts() },
            onSuccess = { productsList ->
                _productUiState.update { it.copy(productsList = productsList) }
                applyFilters()
            }
        )
    }

    fun fetchProductVariantsList(productId: UUID, skipLoading: Boolean = false) {
        performRepositoryAction(
            binding = _productUiState.value.productVariantsListFetched,
            failureMessage = "Could not fetch product variants, try again",
            skipLoading = skipLoading,
            action = { productVariantRepository.getAllProductVariantsByProductId(productId) },
            onSuccess = { productVariantsList ->
                _productUiState.update { it.copy(productVariantsList = productVariantsList) }
                applyFilters()
            }
        )
    }

    fun fetchProductDetails(productId: UUID) {
        performRepositoryAction(
            binding = _productUiState.value.currentAddEditProductState,
            failureMessage = "Could not fetch product details, try again",
            action = {
                val productDetails = productRepository.getProductDetails(productId)
                _productUiState.update { it.copy(currentAddEditProductDetails = productDetails) }
                ProductCreateRequest(productDetails)
            },
        )
    }

    fun addProduct(productCreateRequest: ProductCreateRequest) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Failed to update product",
            action = {
                productRepository.createProduct(productCreateRequest)
            },
            onSuccess = {
                fetchProductsList(true)
            }
        )
    }

    fun updateProduct(productId: UUID, productCreateRequest: ProductCreateRequest) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Failed to update product",
            action = {
                productRepository.updateProduct(
                    productId = productId,
                    request = productCreateRequest
                )
            },
            onSuccess = {
                fetchProductsList(true)
            }
        )
    }

    fun deleteProduct(productId: UUID) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Failed to delete product",
            action = {
                productRepository.deactivateProduct(productId)
            },
            onSuccess = {
                fetchProductsList(true)
            }
        )
    }


    fun getProductVariantDetails(productVariantId: UUID?): ProductVariantDetails? {
        /*TODO*/
        return productVariantId?.let { testProductVariantDetailsList.find { it.productVariantId == productVariantId } }
    }

    fun saveCurrentAddEditProduct(productCreateRequest: ProductCreateRequest) {
        _productUiState.update {
            it.copy(
                currentAddEditProductState = MutableStateFlow(ResultState.Success(productCreateRequest))
            )
        }
    }

    fun updateCurrentAddEditProduct(productId: UUID?) {
        if (productId == null) {
            _productUiState.update {it.copy(currentAddEditProductDetails = null)}
            saveCurrentAddEditProduct(ProductCreateRequest())
        }
        else {
            fetchProductDetails(productId)
        }
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

//    fun removeProductFromList(product: ProductSummary) {
//        /*TODO*/
//        _productUiState.update { currState ->
//            val productIndexToDelete = currState.productsList.indexOf(currState.productsList.find { it.productId == product.productId })
//            val productIndexToDeleteFiltered = currState.filteredSortedProductTemplatesList.indexOf(currState.filteredSortedProductTemplatesList.find { it.productId == product.productId })
//            currState.copy(
//                productsList = currState.productsList.toMutableList().apply {
//                    removeAt(productIndexToDelete)
//                },
//                filteredSortedProductTemplatesList = currState.filteredSortedProductTemplatesList.toMutableList().apply {
//                    removeAt(productIndexToDeleteFiltered)
//                }
//            )
//        }
//    }

    // Helper method for updating state with filters applied
    private fun applyFilters() {
        _productUiState.update { currState ->
            currState.copy(
                filteredSortedProductTemplatesList = if (currState.showProductsList) {
                    productFilter.filterProducts(
                        templates = currState.productsList,
                        searchQuery = currState.searchQuery,
                        filterParams = currState.filterParams
                    )
                } else currState.filteredSortedProductTemplatesList,

                filteredSortedProductVariantsList = if (currState.showProductVariantsList) {
                    productFilter.filterProductVariants(
                        variants = currState.productVariantsList,
                        searchQuery = currState.searchQuery,
                        filterParams = currState.filterParams
//                        selectedTemplate = currState.filteredByProduct,
                    ) } else currState.filteredSortedProductVariantsList
            )
        }
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

    fun showProductVariants(selectedProduct: ProductSummary?) {
        _productUiState.update { currState ->
            currState.copy(
                showProductsList = selectedProduct == null,
                showProductVariantsList = selectedProduct != null,
                filteredByProduct = selectedProduct
            )
        }
        applyFilters()
    }

    // Update filter state with one function call, reducing the need for multiple function parameters
    fun updateFilterParams(filterParams: FilterParams) {
        _productUiState.update { it.copy(
            filterParams = filterParams,
            filterParamsInEdit = filterParams
        ) }
        applyFilters()
    }

    fun saveFilterParamsInEdit(filterParams: FilterParams) {
        _productUiState.update { it.copy(filterParamsInEdit = filterParams) }
    }

    fun cancelFilters() {
        _productUiState.update { it.copy(filterParams = FilterParams()) }
        applyFilters()
    }

    fun clearCategoryFilter() {
        _productUiState.update { it.copy(filterParams = it.filterParams.copy(category = null)) }
        applyFilters()
    }

    fun clearPriceFilter(from: Boolean = true) {
        _productUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    priceFrom = if (from) BigDecimal.ZERO else currState.filterParams.priceFrom,
                    priceTo = if (!from) null else currState.filterParams.priceTo
                ),
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
        val sortingType: SortingType = SortingType.NONE,
        val priceFrom: BigDecimal? = null,
        val priceTo: BigDecimal? = null,
        val category: CategoryTree? = null
    )
}


