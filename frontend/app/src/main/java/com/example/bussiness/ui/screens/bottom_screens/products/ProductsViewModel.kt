package com.example.bussiness.ui.screens.bottom_screens.products

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.bussiness.firebase.FirebaseRepository
import com.example.bussiness.firebase.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductsViewModel : ViewModel() {
    private val _productUiState = MutableStateFlow(ProductsUiState())
    val productUiState: StateFlow<ProductsUiState> = _productUiState.asStateFlow()

    init {
        FirebaseRepository.getProducts { productsList ->
            updateProductsList(productsList)
        }
    }

    fun updateProductsList(productsList: List<Product>) {
        _productUiState.update { currState ->
            currState.copy(productsList = productsList)
        }
    }

    fun deleteProductFromList(productId: String) {
        FirebaseRepository.deleteProduct(productId)
    }

    fun updateProduct(product: Product) {
        FirebaseRepository.upsertProduct(product)
    }

    fun uploadProductImage(uri: Uri, onComplete: (String) -> Unit) {
        FirebaseRepository.uploadImageToFirebase(uri) { url ->
            onComplete(url)
        }
    }

    fun updateAddEditViewState(
        productEditDialogShow: Boolean,
        currentProductInEdit: Product = Product() ) {
        updateCurrentProduct(currentProductInEdit)
        updateDialogShow(productEditDialogShow)
    }

    fun updateDialogShow(dialogIsShowed: Boolean) {
     _productUiState.update { currState ->
         currState.copy(showProductAddEditView = dialogIsShowed)
        }
    }

    fun updateCurrentProduct(currentProductInEdit: Product) {
     _productUiState.update { currState ->
         currState.copy(currentAddEditProduct = currentProductInEdit)
        }
    }

}
