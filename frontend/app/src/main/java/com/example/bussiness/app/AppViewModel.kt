package com.example.bussiness.app

import androidx.lifecycle.ViewModel
import com.example.bussiness.ui.screens.bottom_screens.products.ProductsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel: ViewModel() {
    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()

    fun updateCurrentNavItem(navigationItem: NavigationItem = bottomNavigationBarItems[0]) {
        val isNavElementsVisible = bottomNavigationBarItems.contains(navigationItem)
        _appUiState.update { currState ->
            currState.copy(currentNavigationItem = navigationItem,
                           appNavigationVisibility = isNavElementsVisible)
        }
    }

    fun updateMoreBottomSheetVisibility(isVisible: Boolean) {
        _appUiState.update { currState ->
            currState.copy(moreBottomSheetIsVisible = isVisible)
        }
    }


}