package com.example.amoz.ui.screens.bottom_screens.products

import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amoz.navigation.NavItemType
import com.example.amoz.navigation.productScreenBottomSheetMenu
import com.example.amoz.ui.components.ConfirmDeleteItemBottomSheet
import com.example.amoz.ui.components.filters.MoreFiltersBottomSheet
import com.example.amoz.view_models.ProductsViewModel.BottomSheetType
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditProductBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditProductVariantBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditSimpleProductBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.products_list.FilteredProductsList
import com.example.amoz.view_models.ProductsViewModel


@Composable
fun ProductScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    productsViewModel: ProductsViewModel = hiltViewModel()
) {
    val productsUiState by productsViewModel.productUiState.collectAsState()

    fun onMenuItemClick(
        navItemType: NavItemType
    ) {
        when (navItemType) {
            NavItemType.AddSimpleProduct -> {
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_SIMPLE, true)
            }
            NavItemType.AddProductVariant -> {
                productsViewModel.updateCurrentAddEditProductVariant(null, null)
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_VARIANT, true)
            }
            NavItemType.AddProductTemplate -> {
                productsViewModel.updateCurrentAddEditProduct(null)
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_PRODUCT, true)
            }
            else -> {
                navController.currentBackStackEntry?.savedStateHandle?.set("isSelectable", false)
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
                onProductEdit = {
                    productsViewModel.updateCurrentAddEditProduct(it)
                    productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_PRODUCT, true)
                },
                onProductDelete = {
                    productsViewModel.updateCurrentProductToDelete(it)
                    productsViewModel.expandBottomSheet(BottomSheetType.DELETE_PRODUCT, true)
                },
                onProductVariantEdit = {
                    productsViewModel.updateCurrentAddEditProductVariant(null /*TODO*/, it)
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
                        productsViewModel.expandBottomSheet(BottomSheetType.MENU, true)
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
                    productsViewModel.saveFilterParamsInEdit(productsUiState.filterParams)
                    productsViewModel.expandBottomSheet(BottomSheetType.MORE_FILTERS, false)
                },
                onApplyFilters = productsViewModel::updateFilterParams,
                onSaveFilterParams = productsViewModel::saveFilterParamsInEdit,
                onCancelFilters = productsViewModel::cancelFilters,
                filterParams = productsUiState.filterParamsInEdit,
                navController = navController
            )
        }

        // -------------------- Confirm product delete --------------------
        if (productsUiState.deleteProductBottomSheetExpanded) {
             productsUiState.currentProductToDelete?.let {
                ConfirmDeleteItemBottomSheet(
                    onDismissRequest = {
                        productsViewModel.updateCurrentProductToDelete(null)
                        productsViewModel.expandBottomSheet(BottomSheetType.DELETE_PRODUCT, false)
                    },
                    onDeleteConfirm = {
                        productsViewModel.removeProductFromList(it)
                    },
                    itemNameToDelete = it.name
                )
            }
        }

        // -------------------- Add/Edit Simple Product --------------------
        if (productsUiState.addEditSimpleProductBottomSheetExpanded) {
            productsUiState.currentAddEditProduct?.let {
                AddEditSimpleProductBottomSheet(
                    onDismissRequest = {
                        productsViewModel.updateCurrentAddEditProduct(null)
                        productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_SIMPLE, false)
                    },
                    productVariant = it,
                    onComplete = {},
                )
            }
        }

        // -------------------- Add/Edit Product Variant --------------------
        if (productsUiState.addEditProductVariantBottomSheetExpanded) {
            productsUiState.currentAddEditProductVariant?.let {
                AddEditProductVariantBottomSheet(
                    onDismissRequest = {
                        productsViewModel.updateCurrentAddEditProductVariant(null, null)
                        productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_VARIANT, false)
                    },
                    productVariant = it,
                    onComplete = { }
                )
            }
        }

        // -------------------- Add/Edit Product Template --------------------
        if (productsUiState.addEditProductBottomSheetExpanded) {
            AddEditProductBottomSheet(
                productDetailsState = productsUiState.currentAddEditProductState,
                navController = navController,
                onSaveProduct = productsViewModel::saveCurrentAddEditProduct,
                onComplete = { productState ->
                    productsUiState.currentAddEditProductId?.let {
                        productsViewModel.updateProduct(it, productState)
                    }
                    ?: run {
                        productsViewModel.addProduct(productState)
                    }
                },
                onDismissRequest = {
                    productsViewModel.updateCurrentAddEditProduct(null)
                    productsViewModel.expandBottomSheet(
                        BottomSheetType.ADD_EDIT_PRODUCT,
                        false
                    )
                },
            )
        }
    }
}




