package com.example.bussiness.ui.screens.bottom_screens.selling

import androidx.lifecycle.ViewModel
import com.example.bussiness.firebase.FirebaseRepository
import com.example.bussiness.firebase.Product
import com.example.bussiness.firebase.SoldProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SellingViewModel : ViewModel() {
    private val _sellingUiState = MutableStateFlow(SellingUIState())
    val sellingUiState: StateFlow<SellingUIState> = _sellingUiState.asStateFlow()

    init {
        FirebaseRepository.getSales { salesList ->
            updateSalesList(salesList)
        }
        FirebaseRepository.getProducts { products ->
            updateProductsList(products)
        }
    }

    fun updateSalesList(salesList: List<SoldProduct>) {
        _sellingUiState.update { currState ->
            currState.copy(salesList = salesList, salesListIsLoading = false)
        }
    }

    fun updateProductsList(productsList: List<Product>) {
        _sellingUiState.update { currState ->
            currState.copy(productsList = productsList)
        }
    }
    fun updateSoldProduct(soldProduct: SoldProduct) {
        FirebaseRepository.upsertSale(soldProduct)
    }

    fun updateAddEditViewState(
        productEditDialogShow: Boolean,
        currentSoldProductInEdit: SoldProduct = SoldProduct() ) {
        updateCurrentSaleProduct(currentSoldProductInEdit)
        updateDialogShow(productEditDialogShow)
    }

    fun updateDialogShow(dialogShow: Boolean) {
        _sellingUiState.update { currState ->
            currState.copy(showAddEditSaleDialog = dialogShow)
        }
    }

    fun showFilterBottomSheet(dialogShow: Boolean) {
        _sellingUiState.update { currState ->
            currState.copy(showFilterBottomSheet = dialogShow)
        }
    }

    fun updateCurrentSaleProduct(currentSoldProductInEdit: SoldProduct) {
        _sellingUiState.update { currState ->
            currState.copy(currentAddEditSaleProduct = currentSoldProductInEdit)
        }
    }

}