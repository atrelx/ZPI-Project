package com.example.amoz.ui.states

import com.example.amoz.app.NavItemType
import com.example.amoz.app.bottomNavigationBarNavItemsMap
import com.example.amoz.data.NavItem

data class AppUiState (
    val appNavigationVisibility: Boolean = true,
    val moreBottomSheetIsVisible: Boolean = false,
    val currentNavigationItem: NavItem? = bottomNavigationBarNavItemsMap[NavItemType.Home],
)