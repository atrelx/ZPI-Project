package com.example.bussiness.ui.screens.bottom_screens.orders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.bussiness.app.NavigationItem
import com.example.bussiness.ui.theme.AmozApplicationTheme

@Composable
fun OrdersScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    salesViewModel: OrdersViewModel = viewModel() ) {
    AmozApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val salesUiState by salesViewModel.orderUiState.collectAsState()

            if (salesUiState.showAddEditSaleDialog)
                OrderAddEditVIew(
                    saleProduct = salesUiState.currentAddEditSaleProduct,
                    productList = salesUiState.productsList,
                    onComplete = { salesViewModel.updateSoldProduct(it) },
                    closeAddEditDialog = {
                        salesViewModel.updateAddEditViewState(false)
                    }
                )

            if (!salesUiState.salesListIsLoading) {
                OrdersLazyColumn(
                    paddingValues = paddingValues,
                    salesList = salesUiState.salesList,
                    onSoldProductClick = { saleProduct ->
                        salesViewModel.updateAddEditViewState(true, saleProduct)
                    },
                    filterBottomSheetShowed = salesUiState.showFilterBottomSheet,
                    updateFilterBottomSheetShowState = { salesViewModel.showFilterBottomSheet(it) }
                )
            }
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            ) {
                Column {
                    if (salesUiState.salesListIsLoading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
                ExtendedFloatingActionButton(
                    onClick = {
                        salesViewModel.updateAddEditViewState(true)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd),
                    icon = { Icon(Icons.Filled.Add, "Sale add button.") },
                    text = { Text(text = "Add a Sale") }
                )
            }
        }
    }
}






