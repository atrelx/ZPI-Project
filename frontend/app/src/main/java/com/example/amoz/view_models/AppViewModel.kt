package com.example.amoz.view_models

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import com.example.amoz.navigation.NavItemType
import com.example.amoz.navigation.bottomNavigationBarNavItemsMap
import com.example.amoz.ui.states.AppUiState
import com.example.amoz.app.NavItemType
import com.example.amoz.app.bottomNavigationBarNavItemsMap
import com.example.amoz.data.NavItem
import com.example.amoz.ui.states.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel: ViewModel() {
    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()

    init {

    }

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