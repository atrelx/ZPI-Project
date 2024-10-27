package com.example.bussiness.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Cases
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PermDeviceInformation
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bussiness.R
import com.example.bussiness.ui.screens.Screens

// --------------------- Navigation Item Data Class ---------------------

data class NavigationItem(
    val title: Int,
    val screen: String? = null,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

// --------------------- Bottom Navigation Bar Items ---------------------

val bottomNavigationBarItems = listOf(
    NavigationItem(
        title = R.string.home_screen,
        screen = Screens.Home.route,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    NavigationItem(
        title = R.string.products_screen,
        screen = Screens.Products.route,
        selectedIcon = Icons.Filled.Sell,
        unselectedIcon = Icons.Outlined.Sell,
    ),
    NavigationItem(
        title = R.string.orders_screen,
        screen = Screens.Orders.route,
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart,
    ),
    NavigationItem(
        title = R.string.company_screen,
        screen = Screens.Company.route,
        selectedIcon = Icons.Outlined.Work,
        unselectedIcon = Icons.Filled.Work,
    ),
    NavigationItem(
        title = R.string.more_screen,
        selectedIcon = Icons.Filled.MoreHoriz,
        unselectedIcon = Icons.Outlined.MoreHoriz,
    ),
)
// --------------------- 'More' Button Bar Items ---------------------

val moreBottomSheetItems = listOf(
    NavigationItem(
        title = R.string.settings_screen,
        screen = Screens.Settings.route,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    ),
    NavigationItem(
        title = R.string.faq_screen,
        screen = Screens.FAQ.route,
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Filled.Info,
    ),
    NavigationItem(
        title = R.string.about_screen,
        screen = Screens.About.route,
        selectedIcon = Icons.Outlined.PermDeviceInformation,
        unselectedIcon = Icons.Outlined.PermDeviceInformation,
    ),
    NavigationItem(
        title = R.string.support_contact_screen,
        screen = Screens.Support.route,
        selectedIcon = Icons.Filled.Mail,
        unselectedIcon = Icons.Filled.Mail,
    ),
)
// ------------------- Other Screens In Application (in different parts of app) -------------------

val otherNavigationItems = listOf(
    NavigationItem(
        title = R.string.profile_screen,
        screen = Screens.Profile.route,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Filled.AccountCircle,
    ),
)
// ------------------- Company Screen Info Items -------------------

val companyInfoScreenItems = listOf(
    NavigationItem(
        title = R.string.company_address_screen,
        screen = Screens.CompanyAddress.route,
        selectedIcon = Icons.Outlined.LocationOn,
        unselectedIcon = Icons.Filled.AccountCircle,
    ),
    NavigationItem(
        title = R.string.company_workers_screen,
        screen = Screens.Workers.route,
        selectedIcon = Icons.Outlined.Cases,
        unselectedIcon = Icons.Outlined.Cases,
    ),
    NavigationItem(
        title = R.string.company_customers_screen,
        screen = Screens.Customers.route,
        selectedIcon = Icons.Outlined.People,
        unselectedIcon = Icons.Outlined.People,
    ),
)

val allApplicationScreens = listOf(bottomNavigationBarItems, moreBottomSheetItems,
    companyInfoScreenItems, otherNavigationItems).flatten()
