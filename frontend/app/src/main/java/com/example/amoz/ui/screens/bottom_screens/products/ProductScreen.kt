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
import com.example.amoz.data.ProductVariant
import com.example.amoz.data.ProductTemplate
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditProductTemplateBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditProductVariantBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditSimpleProductBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.lazy_products_list.ProductTemplatesLazyColumn


@Composable
fun ProductScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    productsViewModel: ProductsViewModel = viewModel()
) {
    val productsUiState by productsViewModel.productUiState.collectAsState()

    var currentAddingProductType by remember { mutableStateOf<NavItemType?>(null) }

    AmozApplicationTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductTemplatesLazyColumn(
                productList = productsUiState.productTemplatesList,
                onProductClick = { },
                onProductLongClick = { product ->
                    productsViewModel.deleteProductFromList(product.id)
                })

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
        if (productsUiState.menuBottomSheetExpanded) {
            MenuBottomSheet(
                onDismissRequest = { productsViewModel.expandMenuBottomSheet(false) },
                onNavigateClick = { navItemType ->
                    navController.navigate(
                        productScreenBottomSheetMenu[navItemType]!!.screenRoute
                    )
                },
                onClick = { addingProductType ->
                    currentAddingProductType = addingProductType
                    productsViewModel.expandAddEditProductBottomSheet(true)
                }
            )
        }

        if (productsUiState.addEditProductBottomSheetExpanded && currentAddingProductType != null) {
            when (currentAddingProductType) {
                NavItemType.AddSimpleProduct -> AddEditSimpleProductBottomSheet(
                    onDismissRequest = {
                        currentAddingProductType = null
                        productsViewModel.expandAddEditProductBottomSheet(false)
                    },
                    product = ProductVariant(),
                    onComplete = {},
                    addingProductType = currentAddingProductType!!,
                )
                NavItemType.AddProductVariant -> AddEditProductVariantBottomSheet (
                    onDismissRequest = {
                        productsViewModel.expandAddEditProductBottomSheet(false)
                    }
                )
                NavItemType.AddProductTemplate -> AddEditProductTemplateBottomSheet(
                    onDismissRequest = {
                        productsViewModel.expandAddEditProductBottomSheet(false)
                    },
                    product = ProductTemplate(),
                    onComplete = {},

                )
                else -> { }
            }

        }
    }
}





