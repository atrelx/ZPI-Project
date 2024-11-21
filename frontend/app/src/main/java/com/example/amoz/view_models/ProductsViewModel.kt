package com.example.amoz.view_models

import androidx.lifecycle.ViewModel
import com.example.amoz.data.ProductTemplate
import com.example.amoz.data.ProductVariant
import com.example.amoz.ui.states.ProductsUiState
import com.example.amoz.ui.screens.bottom_screens.products.products_list.ProductListFilter
import com.example.amoz.ui.screens.bottom_screens.products.products_list.testProductTemplatesList
import com.example.amoz.ui.states.ProductsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductsViewModel : ViewModel() {
    private val _productUiState = MutableStateFlow(ProductsUiState())
    val productUiState: StateFlow<ProductsUiState> = _productUiState.asStateFlow()
    private val productFilter = ProductListFilter()

    fun getProductVariantTemplateById (productVariant: ProductVariant): ProductTemplate? {
        /*TODO*/
        return testProductTemplatesList.find { it.id == productVariant.productId }
    }

    // Helper method for updating state with filters applied
    private fun applyFilters() {
        _productUiState.update { currState ->
            currState.copy(
                filteredSortedProductTemplatesList = if (currState.showProductTemplatesList) {
                     productFilter.filterProductTemplates(
                        templates = currState.productTemplatesList,
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
                        getProductVariantTemplateById = ::getProductVariantTemplateById,
                        searchQuery = currState.searchQuery,
                        selectedTemplate = currState.selectedProductTemplate,
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
                filterPriceFrom = 0.0,
                filterPriceTo = Int.MAX_VALUE.toDouble(),
                filterCategory = "",
            )
        }
        applyFilters()
    }

    fun updateAddEditViewState(
        productEditDialogShow: Boolean,
        currentProductInEdit: ProductTemplate = ProductTemplate()
    ) {
        updateCurrentProduct(currentProductInEdit)
        expandBottomSheet(BottomSheetType.MENU, productEditDialogShow)
    }

    fun updateCurrentProduct(currentProductInEdit: ProductTemplate) {
        _productUiState.update { currState ->
            currState.copy(currentAddEditProduct = currentProductInEdit)
        }
    }

    fun removeProductTemplateFromList(productTemplate: ProductTemplate) {
        _productUiState.update { currState ->
            currState.copy(
                productTemplatesList = currState.productTemplatesList - productTemplate,
                filteredSortedProductTemplatesList = currState.filteredSortedProductTemplatesList - productTemplate
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

    fun updateSelectedProductTemplate(selectedProductTemplate: ProductTemplate?) {
        _productUiState.update { currState ->
            currState.copy(
                showProductTemplatesList = selectedProductTemplate == null,
                showProductVariantsList = selectedProductTemplate != null,
                selectedProductTemplate = selectedProductTemplate
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
                filterPriceFrom = if (from) 0.0 else currState.filterPriceFrom,
                filterPriceTo = if (!from) Int.MAX_VALUE.toDouble() else currState.filterPriceTo
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
                addEditProductTemplateBottomSheetExpanded = if (type == BottomSheetType.ADD_EDIT_TEMPLATE) expand else currState.addEditProductTemplateBottomSheetExpanded,
                moreFiltersBottomSheetExpanded = if (type == BottomSheetType.MORE_FILTERS) expand else currState.moreFiltersBottomSheetExpanded,
                deleteProductTemplateBottomSheetExpanded = if (type == BottomSheetType.DELETE_TEMPLATE) expand else currState.deleteProductTemplateBottomSheetExpanded
            )
        }
    }

    enum class BottomSheetType {
        MENU, ADD_EDIT_SIMPLE, ADD_EDIT_VARIANT, ADD_EDIT_TEMPLATE, MORE_FILTERS, DELETE_TEMPLATE
    }

    enum class SortingType {
        NONE, ASCENDING_NAME, DESCENDING_NAME, ASCENDING_PRICE, DESCENDING_PRICE
    }

    data class FilterParams(
        val ascendingSorting: SortingType,
        val priceFrom: Double,
        val priceTo: Double,
        val category: String
    )
}


