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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amoz.data.AppPreferences
import com.example.amoz.data.ProductTemplate
import com.example.amoz.data.ProductVariant
import com.example.amoz.ui.commonly_used_components.SearchTextField
import com.example.amoz.ui.screens.bottom_screens.products.ProductsViewModel
import com.example.amoz.ui.screens.bottom_screens.products.products_list.list_items.ProductTemplateListItem
import com.example.amoz.ui.screens.bottom_screens.products.products_list.list_items.ProductVariantListItem

@Composable
fun FilteredProductsList(
    onMoreFiltersClick: () -> Unit,
    onProductTemplateEdit: (ProductTemplate) -> Unit,
    onProductTemplateDelete: (ProductTemplate) -> Unit,
    onProductVariantEdit: (ProductVariant) -> Unit,
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
                onSearchQueryChange = { productsViewModel.updateSearchQuery(it) },
                onMoreFiltersClick = onMoreFiltersClick
            )
            FilterChips(
                productTemplateChipValue = productsListUiState.selectedProductTemplate?.name,
                onProductTemplateChipClick = {
                    productsViewModel.updateSelectedProductTemplate(null)
                },
                priceFrom = productsListUiState.filterPriceFrom,
                onPriceFromClick = { productsViewModel.clearPriceFilter(true) },
                priceTo = productsListUiState.filterPriceTo,
                onPriceToClick = { productsViewModel.clearPriceFilter(false) }
            )
        }

        // -------------------- ProductTemplates list --------------------
        if (productsListUiState.showProductTemplatesList) {
            items(
                productsListUiState.filteredSortedProductTemplatesList,
                key = { it.id }
            ) { productTemplate ->
                Box(
                    modifier = Modifier.animateItem()
                ) {
                    ProductTemplateListItem(
                        productTemplate = productTemplate,
                        onClick = {
                            productsViewModel.updateSelectedProductTemplate(productTemplate)
                        },
                        onProductRemove = onProductTemplateDelete,
                        onProductTemplateEdit = onProductTemplateEdit,
                        currency = currency!!,
                    )
                }
            }
        }

        // -------------------- ProductVariant list --------------------
        if(productsListUiState.showProductVariantsList) {
            items(
                productsListUiState.filteredSortedProductVariantsList,
                key = { it.id }
            ) { productVariant ->
                Box(modifier = Modifier.animateItem()
                ) {
                    productsViewModel.getProductVariantTemplateById(productVariant)?.let {
                        ProductVariantListItem(
                            productVariant = productVariant,
                            productVariantTemplate = it,
                            currency = currency!!,
                            onClick = { onProductVariantEdit(productVariant) }
                        )
                    }
                }
            }
        }
    }
}





