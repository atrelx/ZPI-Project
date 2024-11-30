package com.example.amoz.view_models

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.amoz.api.repositories.ProductRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.app.AppPreferences
import com.example.amoz.models.CategoryTree
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.ui.states.ProductsUiState
import com.example.amoz.ui.screens.bottom_screens.products.products_list.ProductListFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val productVariantRepository: ProductVariantRepository,
    private val appPreferences: AppPreferences
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


    // -------------------- Fetch products lists --------------------
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

    // -------------------- PRODUCT --------------------
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

    fun updateCurrentProductToDelete(productSummary: ProductSummary?) {
        _productUiState.update { it.copy(currentProductToDelete = productSummary) }
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

    // -------------------- PRODUCT VARIANT--------------------

    fun createProductVariant(productVariantCreateRequest: ProductVariantCreateRequest) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Failed to create product variant",
            action = {
                productVariantRepository.createProductVariant(productVariantCreateRequest)
            },
            onSuccess = productVariantCreateRequest.productID?.let {
                productId -> {fetchProductVariantsList(productId, true)}
            }
        )
    }

    fun updateProductVariant(productVariantId: UUID, productVariantCreateRequest: ProductVariantCreateRequest) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Failed to update product variant",
            action = {
                productVariantRepository.updateProductVariant(productVariantId, productVariantCreateRequest)
            },
            onSuccess = productVariantCreateRequest.productID?.let {
                    productId -> {fetchProductVariantsList(productId, true)}
            }
        )
    }

    fun uploadProductVariantImage(productVariantId: UUID, image: MultipartBody.Part) {
        performRepositoryAction(
            binding = null,
            action = {
                productVariantRepository.uploadProductVariantPicture(productVariantId, image)
            },
            onSuccess = {
                _productUiState.value.filteredByProduct?.let {
                    fetchProductVariantsList(it.productId, true)
                }
            }
        )
    }

    fun getProductVariantPicture(productVariantId: UUID, skipLoading: Boolean = false) {
        val productVariantPicturesMap = _productUiState.value.productVariantImages.value
        if (!productVariantPicturesMap.containsKey(productVariantId)) {
            productVariantPicturesMap[productVariantId] = MutableStateFlow(ResultState.Idle)
        }
        productVariantPicturesMap[productVariantId]?.let {
            performRepositoryAction(
                binding = it,
                skipLoading = skipLoading,
                failureMessage = "Could fetch product variant picture. Try again later.",
                action = {
                    productVariantRepository.getProductVariantPicture(productVariantId)
                }
            )
        }
    }

    fun fetchProductVariantDetails(productVariantId: UUID, productId: UUID) {
        performRepositoryAction(
            binding = _productUiState.value.currentAddEditProductVariantState,
            failureMessage = "Failed to fetch product variant",
            action = {
                val productDetails = productRepository.getProductDetails(productId)
                val productVariantDetails = productVariantRepository.getProductVariant(productVariantId)
                _productUiState.update {
                    it.copy(
                        currentAddEditProductVariantDetails = productVariantDetails,
                        currentAddEditProductDetails = productDetails
                    )
                }
                ProductVariantCreateRequest(productVariantDetails, productId)
            },
            onSuccess = {
                fetchProductVariantsList(productId)
            }
        )
    }

    fun deleteProductVariant(productVariantId: UUID) {
        performRepositoryAction(
            binding = null,
            action = {
                productVariantRepository.deactivateProductVariant(productVariantId)
            },
            onSuccess = {
                _productUiState.value.filteredByProduct?.let {
                    fetchProductVariantsList(it.productId, true)
                }
            }
        )
    }

    fun saveCurrentAddEditProductVariant(productVariantCreateRequest: ProductVariantCreateRequest) {
        _productUiState.update {
            it.copy(
                currentAddEditProductVariantState =
                MutableStateFlow(ResultState.Success(productVariantCreateRequest))
            )
        }
    }

    fun updateCurrentAddEditProductVariant(productVariantId: UUID?, productId: UUID?) {
        if (productVariantId == null || productId == null) {
            Log.d("PRODUCT VARIANT", "was created")
            _productUiState.update {it.copy(currentAddEditProductVariantDetails = null)}
            saveCurrentAddEditProductVariant(ProductVariantCreateRequest(productId))
        }
        else {
            Log.d("PRODUCT VARIANT", "was fetched")
            fetchProductVariantDetails(productVariantId, productId)
        }
    }



    fun updateCurrentProductVariantToDelete(productVariantSummary: ProductVariantSummary?) {
        _productUiState.update { it.copy(currentProductVariantToDelete = productVariantSummary) }
    }

    // -------------------- SIMPLE PRODUCT --------------------
    fun createSimpleProduct(
        product: ProductCreateRequest,
        productVariant: ProductVariantCreateRequest
    ) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Cannot create simple product",
            action = {
                Log.d("SIMPLE PRODUCT", product.toString())
                productRepository.createProduct(product)
            },
            onSuccess = { productDetails ->
                performRepositoryAction(
                    binding = null,
                    action = {
                        productVariantRepository.createProductVariant(
                            productVariant.copy(productID = productDetails.productId)
                        )
                    },
                    onSuccess = { productVariantDetails ->
                        performRepositoryAction(
                            binding = null,
                            action = {
                                productRepository.setMainVariant(
                                    productDetails.productId,
                                    productVariantDetails.productVariantId
                                )
                            },
                            onSuccess = {
                                fetchProductsList(true)
                            }
                        )
                    }
                )
            }
        )
    }

    fun saveSimpleProduct(
        simpleProduct: Pair<ProductCreateRequest, ProductVariantCreateRequest>?
    ) {
        var simpleProductToSave = simpleProduct
        if (simpleProductToSave == null) {
            simpleProductToSave = Pair(ProductCreateRequest(), ProductVariantCreateRequest())
        }
        _productUiState.update {
            it.copy(
                currentAddEditSimpleProduct = simpleProductToSave
            )
        }
    }


    // -------------------- FILTERS--------------------
    private fun applyFilters() {
        _productUiState.update { currState ->
            currState.copy(
                filteredSortedProductsList = if (currState.showProductsList) {
                    productFilter.filterProducts(
                        templates = currState.productsList,
                        searchQuery = currState.searchQuery,
                        filterParams = currState.filterParams
                    )
                } else currState.filteredSortedProductsList,

                filteredSortedProductVariantsList = if (currState.showProductVariantsList) {
                    productFilter.filterProductVariants(
                        variants = currState.productVariantsList,
                        searchQuery = currState.searchQuery,
                        filterParams = currState.filterParams
                    )
                } else currState.filteredSortedProductVariantsList
            )
        }
    }



    fun updateSearchQuery(query: String) {
        _productUiState.update { currState ->
            currState.copy(
                searchQuery = query,
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

    fun clearPriceFilter(from: Boolean) {
        _productUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    priceFrom = if (from) null else currState.filterParams.priceFrom,
                    priceTo = if (!from) null else currState.filterParams.priceTo
                ),
            )
        }
        applyFilters()
    }

    // -------------------- UI STATE --------------------

    fun getCurrency(): Flow<String?> {
        return appPreferences.currency
    }

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


