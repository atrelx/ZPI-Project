package com.example.bussiness.ui.screens.bottom_screens.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
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
import com.example.bussiness.ui.theme.AmozApplicationTheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ProductScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    productsViewModel: ProductsViewModel = viewModel()
) {
    val productsUiState by productsViewModel.productUiState.collectAsState()

    AmozApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            ProductsLazyColumn(
                paddingValues = paddingValues,
                productList = productsUiState.productsList,
                onProductClick = { },
                onProductLongClick = { product ->
                    productsViewModel.deleteProductFromList(product.id)
                })

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
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
                onDismissRequest = { productsViewModel.expandMenuBottomSheet(false) }
            )
        }
    }
}

enum class FeatureType {
    TEXT_FIELD, LIST
}







