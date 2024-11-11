package com.example.amoz.ui.screens.bottom_screens.products

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.amoz.data.ProductTemplate
import com.example.amoz.firebase.FirebaseRepository
import com.example.amoz.data.ProductVariant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductsViewModel : ViewModel() {
    private val _productUiState = MutableStateFlow(ProductsUiState())
    val productUiState: StateFlow<ProductsUiState> = _productUiState.asStateFlow()

    init {
//        FirebaseRepository.getProducts { productsList ->
//            updateProductsList(productsList)
//        }
    }

    fun updateProductsList(productsList: List<ProductTemplate>) {
        _productUiState.update { currState ->
            currState.copy(productTemplatesList = productsList)
        }
    }

    fun deleteProductFromList(productId: String) {
        FirebaseRepository.deleteProduct(productId)
    }

//    fun updateProduct(product: Product) {
//        FirebaseRepository.upsertProduct(product)
//    }

    fun uploadProductImage(uri: Uri, onComplete: (String) -> Unit) {
        FirebaseRepository.uploadImageToFirebase(uri) { url ->
            onComplete(url)
        }
    }

    fun updateAddEditViewState(
        productEditDialogShow: Boolean,
        currentProductInEdit: ProductTemplate = ProductTemplate()
    ) {
        updateCurrentProduct(currentProductInEdit)
        expandMenuBottomSheet(productEditDialogShow)
    }

    fun updateCurrentProduct(currentProductInEdit: ProductTemplate) {
        _productUiState.update { currState ->
            currState.copy(currentAddEditProduct = currentProductInEdit)
        }
    }

    fun expandMenuBottomSheet(dialogIsShowed: Boolean) {
     _productUiState.update { currState ->
         currState.copy(menuBottomSheetExpanded = dialogIsShowed)
        }
    }

    fun expandAddEditProductBottomSheet(dialogIsShowed: Boolean) {
        _productUiState.update { currState ->
            currState.copy(addEditProductBottomSheetExpanded = dialogIsShowed)
        }
    }



}
