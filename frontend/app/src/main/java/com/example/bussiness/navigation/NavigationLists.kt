package com.example.bussiness.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cases
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Cases
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.PermDeviceInformation
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bussiness.ui.screens.Screens

data class NavigationItem(
    val title: String,
    val screen: String? = null,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val bottomNavigationBarItems = listOf(
    NavigationItem(
        title = "Home",
        screen = Screens.Home.route,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    NavigationItem(
        title = "Products",
        screen = Screens.Products.route,
        selectedIcon = Icons.Filled.Sell,
        unselectedIcon = Icons.Outlined.Sell,
    ),
    NavigationItem(
        title = "Selling",
        screen = Screens.Selling.route,
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart,
    ),
    NavigationItem(
        title = "Company",
        screen = Screens.Expanses.route,
        selectedIcon = Icons.Filled.Cases,
        unselectedIcon = Icons.Outlined.Cases,
    ),
    NavigationItem(
        title = "More",
        selectedIcon = Icons.Filled.MoreHoriz,
        unselectedIcon = Icons.Outlined.MoreHoriz,
    ),
)

val moreBottomSheetItems = listOf(
    NavigationItem(
        title = "Settings",
        screen = Screens.Settings.route,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    ),
    NavigationItem(
        title = "FAQ",
        screen = Screens.FAQ.route,
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Filled.Info,
    ),
    NavigationItem(
        title = "About App",
        screen = Screens.About.route,
        selectedIcon = Icons.Outlined.PermDeviceInformation,
        unselectedIcon = Icons.Outlined.PermDeviceInformation,
    ),
    NavigationItem(
        title = "Support & Contact",
        screen = Screens.Support.route,
        selectedIcon = Icons.Filled.Mail,
        unselectedIcon = Icons.Filled.Mail,
    ),
)