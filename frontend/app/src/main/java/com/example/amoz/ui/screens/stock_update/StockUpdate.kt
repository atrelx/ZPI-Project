package com.example.amoz.ui.screens.stock_update

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amoz.view_models.StockViewModel

@Composable
fun StockUpdate(
    paddingValues: PaddingValues,
    stockViewModel: StockViewModel = hiltViewModel()
) {
    val stockUiState by stockViewModel.stockUpdateUiState.collectAsState()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (stockUiState.productVariants.isEmpty()) {
            EmptyStockLayout(
                onAdd = {}
            )
        }
    }
}





