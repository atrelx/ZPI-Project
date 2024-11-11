package com.example.amoz.app

import com.example.amoz.data.NavItem

data class AppUiState (
    val appNavigationVisibility: Boolean = true,
    val moreBottomSheetIsVisible: Boolean = false,
    val currentNavigationItem: NavItem? = bottomNavigationBarNavItemsMap[NavItemType.Home],
)