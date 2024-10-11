package com.example.bussiness.screens.drawer_screens.expanses

import androidx.lifecycle.ViewModel
import com.example.bussiness.screens.drawer_screens.products.ProductsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpansesTemplatesViewModel: ViewModel() {
    private val _expansesTemplateUiState = MutableStateFlow(ProductsUiState())
    val expansesTemplateUiState: StateFlow<ProductsUiState> = _expansesTemplateUiState.asStateFlow()
}