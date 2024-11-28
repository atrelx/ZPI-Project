package com.example.amoz.ui.screens.bottom_screens.products.products_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amoz.R
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.data.AppPreferences
import com.example.amoz.models.ProductSummary
import com.example.amoz.ui.components.EmptyLayout
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.components.text_fields.SearchTextField
import com.example.amoz.view_models.ProductsViewModel
import com.example.amoz.ui.screens.bottom_screens.products.products_list.list_items.ProductListItem
import com.example.amoz.ui.screens.bottom_screens.products.products_list.list_items.ProductVariantListItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

@Composable
fun FilteredProductsList(
    onMoreFiltersClick: () -> Unit,
    onProductEdit: (UUID) -> Unit,
    onProductClick: (ProductSummary) -> Unit,
    onProductDelete: (ProductSummary) -> Unit,
    onProductVariantEdit: (UUID, UUID) -> Unit,
    onProductVariantAdd: (UUID) -> Unit,
    productsViewModel: ProductsViewModel = hiltViewModel()
) {
    val productsUiState by productsViewModel.productUiState.collectAsState()
    val currContext = LocalContext.current
    val appPreferences = remember { AppPreferences(currContext) }

    val currency by appPreferences.currency.collectAsState(initial = "USD")

    val stateView: MutableStateFlow<ResultState<List<Any>>> =
        if (productsUiState.filteredByProduct == null) {
            productsUiState.productsListFetched as MutableStateFlow<ResultState<List<Any>>>
        } else {
            productsUiState.productVariantsListFetched as MutableStateFlow<ResultState<List<Any>>>
        }


    ResultStateView(
        state = stateView,
        onPullToRefresh = {
            if (productsUiState.filteredByProduct != null) {
                productsViewModel.fetchProductVariantsList(
                    productId = productsUiState.filteredByProduct!!.productId,
                    skipLoading = true
                )
            }
            else {
                productsViewModel.fetchProductsList(skipLoading = true)
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            val showProducts = productsUiState.showProductsList
            val showProductVariants = productsUiState.showProductVariantsList

            val productsList = productsUiState.filteredSortedProductsList
            val variantsList = productsUiState.filteredSortedProductVariantsList
            // -------------------- Search text field, filter chips --------------------
            item {
                SearchTextField(
                    searchQuery = productsUiState.searchQuery,
                    placeholder = stringResource(id = R.string.search_products_placeholder),
                    onSearchQueryChange = { productsViewModel.updateSearchQuery(it) },
                    onMoreFiltersClick = onMoreFiltersClick
                )
                FilterChips(
                    productTemplateChipValue = productsUiState.filteredByProduct?.name,
                    onProductTemplateChipClick = {
                        productsViewModel.showProductVariants(null)
                    },
                    category = productsUiState.filterParams.category,
                    onCategoryClick = productsViewModel::clearCategoryFilter,
                    priceFrom = productsUiState.filterParams.priceFrom,
                    onPriceFromClick = { productsViewModel.clearPriceFilter(true) },
                    priceTo = productsUiState.filterParams.priceTo,
                    onPriceToClick = { productsViewModel.clearPriceFilter(false) }
                )
            }


            // -------------------- Products list --------------------
            if (showProducts) {

                items(productsList, key = { it.productId }) { product ->
                    Box(
                        modifier = Modifier.animateItem()
                    ) {
                        ProductListItem(
                            product = product,
                            onClick = { onProductClick(product) },
                            onProductRemove = onProductDelete,
                            onProductEdit = onProductEdit,
                            currency = currency!!,
                        )
                    }
                }
                if(productsList.isEmpty()) { item {EmptyLayout {}} }
            }

            // -------------------- ProductVariant list --------------------
            if (showProductVariants) {
                items(variantsList, key = { it.productVariantId }) { productVariant ->
                    Box(modifier = Modifier.animateItem()) {
                        ProductVariantListItem(
                            productVariant = productVariant,
                            currency = currency!!,
                            onClick = {
                                productsUiState.filteredByProduct?.let {
                                    onProductVariantEdit(productVariant.productVariantId, it.productId)
                                }
                            }
                        )
                    }
                }
                if(variantsList.isEmpty()) { item {EmptyLayout {} } }
            }
            item {
                PrimaryFilledButton(
                    onClick = {
                        productsUiState.filteredByProduct?.let {
                            onProductVariantAdd(it.productId)
                        }
                    },
                    text =
                    if (showProductVariants) stringResource(R.string.add_product_variant)
                    else stringResource(R.string.products_add_product),
                    leadingIcon = Icons.Default.Add
                )
            }
        }
    }
}





