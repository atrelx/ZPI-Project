package com.example.bussiness.screens.drawer_screens.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bussiness.ui.theme.BussinessTheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ProductScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    productsViewModel: ProductsViewModel = viewModel()
) {
    BussinessTheme {
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

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.BottomEnd
            ) {

                ProductsLazyColumn(
                    paddingValues = paddingValues,
                    productList = productsUiState.productsList,
                    onProductClick = { product ->
                        productsViewModel.updateAddEditViewState(true, product)
                    },
                    onProductLongClick = { product ->
                        productsViewModel.deleteProductFromList(product.id)
                    }
                )

                ExtendedFloatingActionButton(
                    onClick = {
                        productsViewModel.updateAddEditViewState(true)
                    },
                    modifier = Modifier.padding(16.dp),
                    icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                    text = { Text(text = "New Product") }
                )
            }
        }
    }
}





enum class FeatureType {
    TEXT_FIELD, LIST
}







