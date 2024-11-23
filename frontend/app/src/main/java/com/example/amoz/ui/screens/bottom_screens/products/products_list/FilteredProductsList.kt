package com.example.amoz.ui.screens.bottom_screens.products.products_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amoz.R
import com.example.amoz.data.AppPreferences
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.ui.components.SearchTextField
import com.example.amoz.view_models.ProductsViewModel
import com.example.amoz.ui.screens.bottom_screens.products.products_list.list_items.ProductListItem
import com.example.amoz.ui.screens.bottom_screens.products.products_list.list_items.ProductVariantListItem
import java.util.UUID

@Composable
fun FilteredProductsList(
    onMoreFiltersClick: () -> Unit,
    onProductEdit: (UUID) -> Unit,
    onProductDelete: (ProductSummary) -> Unit,
    onProductVariantEdit: (UUID) -> Unit,
    productsViewModel: ProductsViewModel = viewModel()
) {
    val productsListUiState by productsViewModel.productUiState.collectAsState()
    val currContext = LocalContext.current
    val appPreferences = remember { AppPreferences(currContext) }

    val currency by appPreferences.currency.collectAsState(initial = "USD")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        // -------------------- Search text field, filter chips --------------------
        item {
            SearchTextField(
                searchQuery = productsListUiState.searchQuery,
                placeholder = stringResource(id = R.string.search_products_placeholder),
                onSearchQueryChange = { productsViewModel.updateSearchQuery(it) },
                onMoreFiltersClick = onMoreFiltersClick
            )
            FilterChips(
                productTemplateChipValue = productsListUiState.filteredByProduct?.name,
                onProductTemplateChipClick = {
                    productsViewModel.updateFilteredByProduct(null)
                },
                priceFrom = productsListUiState.filterPriceFrom,
                onPriceFromClick = { productsViewModel.clearPriceFilter(true) },
                priceTo = productsListUiState.filterPriceTo,
                onPriceToClick = { productsViewModel.clearPriceFilter(false) }
            )
        }

        // -------------------- ProductTemplates list --------------------
        if (productsListUiState.showProductsList) {
            items(
                productsListUiState.filteredSortedProductTemplatesList,
                key = { it.productId }
            ) { productTemplate ->
                Box(
                    modifier = Modifier.animateItem()
                ) {
                    ProductListItem(
                        product = productTemplate,
                        onClick = {
                            productsViewModel.updateFilteredByProduct(productTemplate)
                        },
                        onProductRemove = onProductDelete,
                        onProductTemplateEdit = onProductEdit,
                        currency = currency!!,
                    )
                }
            }
        }

        // -------------------- ProductVariant list --------------------
        if(productsListUiState.showProductVariantsList) {
            items(
                productsListUiState.filteredSortedProductVariantsList,
                key = { it.productVariantId }
            ) { productVariant ->
                Box(modifier = Modifier.animateItem()
                ) {
                    ProductVariantListItem(
                        productVariant = productVariant,
                        currency = currency!!,
                        onClick = { onProductVariantEdit(productVariant.productVariantId) }
                    )
                }
            }
        }
    }
}





