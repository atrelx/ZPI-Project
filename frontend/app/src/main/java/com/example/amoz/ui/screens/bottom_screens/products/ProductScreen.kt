package com.example.amoz.ui.screens.bottom_screens.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.ui.theme.AmozApplicationTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amoz.app.NavItemType
import com.example.amoz.app.productScreenBottomSheetMenu
import com.example.amoz.data.ProductTemplate
import com.example.amoz.data.ProductVariant
import com.example.amoz.view_models.ProductsViewModel.BottomSheetType
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditProductTemplateBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditProductVariantBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditSimpleProductBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.products_list.FilteredProductsList
import com.example.amoz.view_models.ProductsViewModel


@Composable
fun ProductScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    productsViewModel: ProductsViewModel = viewModel()
) {
    val productsUiState by productsViewModel.productUiState.collectAsState()

    var currentProductTemplate by remember { mutableStateOf<ProductTemplate?>(null) }
    var currentProductVariant by remember { mutableStateOf<ProductVariant?>(null) }

    fun onMenuItemClick(
        navItemType: NavItemType
    ) {
        when (navItemType) {
            NavItemType.AddSimpleProduct -> {
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_SIMPLE, true)
            }
            NavItemType.AddProductVariant -> {
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_VARIANT, true)

            }
            NavItemType.AddProductTemplate -> {
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_TEMPLATE, true)
            }
            else -> {
                navController.navigate(
                    productScreenBottomSheetMenu[navItemType]!!.screenRoute
                )
            }
        }
    }

    AmozApplicationTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 5.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            // -------------------- Products --------------------
            FilteredProductsList(
                onProductTemplateEdit = { productTemplate ->
                    currentProductTemplate = productTemplate
                    productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_TEMPLATE, true)
                },
                onProductTemplateDelete = {
                    currentProductTemplate = it
                    productsViewModel.expandBottomSheet(BottomSheetType.DELETE_TEMPLATE, true)
                },
                onProductVariantEdit = {
                    currentProductVariant = it
                    productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_VARIANT, true)
                },
                onMoreFiltersClick = {
                    productsViewModel.expandBottomSheet(BottomSheetType.MORE_FILTERS, true)
                }
            )

            // -------------------- Menu FAB --------------------
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        productsViewModel.updateAddEditViewState(true)
                    },
                    modifier = Modifier
                        .padding(16.dp),
                    icon = { Icon(Icons.Filled.Menu, null) },
                    text = { Text(text = "Menu") }
                )
            }

        }
        // -------------------- Menu Bottom Sheet --------------------
        if (productsUiState.menuBottomSheetExpanded) {
            MenuBottomSheet(
                onDismissRequest = {
                    productsViewModel.expandBottomSheet(BottomSheetType.MENU, false)
                },
                onClick = { onMenuItemClick(it) },
            )
        }

        // -------------------- More product filters Bottom Sheet --------------------
        if (productsUiState.moreFiltersBottomSheetExpanded) {
            MoreFiltersBottomSheet (
                onDismissRequest = {
                    productsViewModel.expandBottomSheet(BottomSheetType.MORE_FILTERS, false)
                },
                onApplyFilters = productsViewModel::updateFilters,
                onCancelFilters = productsViewModel::cancelFilters,
                priceFrom = productsUiState.filterPriceFrom,
                priceTo = productsUiState.filterPriceTo,
                sortingType = productsUiState.sortingType,
                category = productsUiState.filterCategory
            )
        }

        if (productsUiState.deleteProductTemplateBottomSheetExpanded) {
            currentProductTemplate?.let {
                ConfirmDeleteProductBottomSheet(
                    onDismissRequest = {
                        currentProductTemplate = null
                        productsViewModel.expandBottomSheet(BottomSheetType.DELETE_TEMPLATE, false)
                    },
                    onDeleteConfirm = {
                        productsViewModel.removeProductTemplateFromList(it)
                    },
                    productNameToDelete = it.name
                )
            }
        }

        // -------------------- Add/Edit Simple Product --------------------
        if (productsUiState.addEditSimpleProductBottomSheetExpanded) {
            AddEditSimpleProductBottomSheet(
                onDismissRequest = {
                    currentProductVariant = null
                    productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_SIMPLE, false)
                },
                productVariant = currentProductVariant ?: ProductVariant(),
                onComplete = {},
            )
        }

        // -------------------- Add/Edit Product Variant --------------------
        if (productsUiState.addEditProductVariantBottomSheetExpanded) {
            currentProductVariant?.let {
                AddEditProductVariantBottomSheet (
                    onDismissRequest = {
                        currentProductVariant = null
                        productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_VARIANT, false)
                    },
                    productVariant = it,
                    onComplete = {  }
                )
            }
        }

        // -------------------- Add/Edit Product Template --------------------
        if (productsUiState.addEditProductTemplateBottomSheetExpanded) {
            AddEditProductTemplateBottomSheet(
                onDismissRequest = {
                    currentProductTemplate = null
                    productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_TEMPLATE, false)
                },
                product = currentProductTemplate ?: ProductTemplate(),
                onComplete = {},
            )
        }
    }
}




