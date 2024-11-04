package com.example.bussiness.app

import androidx.lifecycle.ViewModel
import com.example.bussiness.data.NavItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel: ViewModel() {
    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()

    fun updateCurrentNavItem(
        navigationItem: NavItem? = bottomNavigationBarNavItemsMap[NavItemType.Home]) {
        val isNavElementsVisible = bottomNavigationBarNavItemsMap.values.contains(navigationItem)
        _appUiState.update { currState ->
            currState.copy(
                currentNavigationItem = navigationItem,
                appNavigationVisibility = isNavElementsVisible
            )
        }
    }

    fun updateMoreBottomSheetVisibility(isVisible: Boolean) {
        _appUiState.update { currState ->
            currState.copy(moreBottomSheetIsVisible = isVisible)
        }
    }


}