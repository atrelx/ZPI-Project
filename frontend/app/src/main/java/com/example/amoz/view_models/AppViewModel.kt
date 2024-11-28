package com.example.amoz.view_models

import androidx.lifecycle.ViewModel
import com.example.amoz.navigation.NavItemType
import com.example.amoz.navigation.bottomNavigationBarNavItemsMap
import com.example.amoz.data.NavItem
import com.example.amoz.ui.states.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel: ViewModel() {
    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()

    fun updateCurrentNavItem(
        navigationItem: NavItem? = bottomNavigationBarNavItemsMap[NavItemType.Home],
        isNavVisible: Boolean? = null
    ) {
        var isNavElementsVisible = bottomNavigationBarNavItemsMap.values.contains(navigationItem)
        isNavVisible?.let { isNavElementsVisible = it }
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