package com.example.bussiness.ui.screens.bottom_screens.company

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CompanyScreenViewModel: ViewModel() {
    private val _companyUiState = MutableStateFlow(CompanyScreenUiState())
    val companyUiState: StateFlow<CompanyScreenUiState> = _companyUiState.asStateFlow()

    init {

    }

    fun updateCompanyAddress(companyAddress: String) {
        _companyUiState.update { currState ->
            currState.copy(companyAddress = companyAddress)
        }
    }

    fun updateCompanyNameDescription(name: String, description: String) {
        _companyUiState.update { currState ->
            currState.copy(companyName = name, companyDescription = description)
        }
    }

    fun updateCompanyNipRegon(nip: String, regon: String) {
        _companyUiState.update { currState ->
            currState.copy(companyNumber = nip, companyRegon = regon)
        }
    }
}