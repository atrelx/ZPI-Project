package com.example.bussiness.app

import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

data class AppUiState (
    val appNavigationVisibility: Boolean = true,
    val moreBottomSheetIsVisible: Boolean = false,
    val currentNavigationItem: NavigationItem = bottomNavigationBarItems[0],
)