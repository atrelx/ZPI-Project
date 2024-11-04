package com.example.bussiness.ui.screens.bottom_screens.products

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
import com.example.bussiness.ui.theme.AmozApplicationTheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ProductScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    productsViewModel: ProductsViewModel = viewModel()
) {
    AmozApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val productsUiState by productsViewModel.productUiState.collectAsState()

            if (productsUiState.showProductAddEditView)
                ProductAddEditDetailsVIew(
                    product = productsUiState.currentAddEditProduct,
                    onComplete = { productsViewModel.updateProduct(product = it) },
                    onImageUpload = { uri, onComplete ->
                        productsViewModel.uploadProductImage(uri, onComplete) },
                    showAddEditDialog = {
                        productsViewModel.updateAddEditViewState(false)
                    }
                )

            ProductsLazyColumn(
                paddingValues = paddingValues,
                productList = productsUiState.productsList,
                onProductClick = { product ->
                    productsViewModel.updateAddEditViewState(true, product)
                },
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
                        .padding(16.dp), // Padding for spacing from screen edges
                    icon = { Icon(Icons.Filled.Menu, null) },
                    text = { Text(text = "Menu") }
                )
            }

        }
    }
}





enum class FeatureType {
    TEXT_FIELD, LIST
}







