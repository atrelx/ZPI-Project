package com.example.amoz.view_models

import androidx.lifecycle.ViewModel
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.ui.states.StockUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class StockViewModel: ViewModel() {
    private val _stockUpdateUiState = MutableStateFlow(StockUiState())
    val stockUpdateUiState: StateFlow<StockUiState> = _stockUpdateUiState.asStateFlow()


}