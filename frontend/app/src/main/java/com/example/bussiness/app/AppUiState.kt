package com.example.bussiness.app

import com.example.bussiness.data.NavItem

data class AppUiState (
    val appNavigationVisibility: Boolean = true,
    val moreBottomSheetIsVisible: Boolean = false,
    val currentNavigationItem: NavItem? = bottomNavigationBarNavItemsMap[NavItemType.Home],
)