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
import androidx.compose.material.icons.outlined.WorkOutline
import com.example.bussiness.R
import com.example.bussiness.data.NavItem
import com.example.bussiness.ui.screens.Screens


// --------------------- Bottom Navigation Bar Items ---------------------

val bottomNavigationBarNavItemsMap = mapOf(
    NavItemType.Home to
            NavItem(
                title = R.string.home_screen,
                screenRoute = Screens.Home.route,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),

    NavItemType.Products to
            NavItem(
                title = R.string.products_screen,
                screenRoute = Screens.Products.route,
                selectedIcon = Icons.Filled.Sell,
                unselectedIcon = Icons.Outlined.Sell,
            ),

    NavItemType.Orders to
            NavItem(
                title = R.string.orders_screen,
                screenRoute = Screens.Orders.route,
                selectedIcon = Icons.Filled.ShoppingCart,
                unselectedIcon = Icons.Outlined.ShoppingCart,
            ),

    NavItemType.Company to
            NavItem(
                title = R.string.company_screen,
                screenRoute = Screens.Company.route,
                selectedIcon = Icons.Filled.Work,
                unselectedIcon = Icons.Outlined.WorkOutline,
            ),

    NavItemType.More to
            NavItem(
                title = R.string.more_screen,
                selectedIcon = Icons.Filled.MoreHoriz,
                unselectedIcon = Icons.Outlined.MoreHoriz,
            )
)


// --------------------- 'More' Button Bar Items ---------------------

val moreBottomSheetItemsMap = mapOf(
    NavItemType.Settings to
            NavItem(
                title = R.string.settings_screen,
                screenRoute = Screens.Settings.route,
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
            ),

    NavItemType.FAQ to
            NavItem(
                title = R.string.faq_screen,
                screenRoute = Screens.FAQ.route,
                selectedIcon = Icons.Filled.Info,
                unselectedIcon = Icons.Filled.Info,
            ),

    NavItemType.AboutApp to
            NavItem(
                title = R.string.about_screen,
                screenRoute = Screens.About.route,
                selectedIcon = Icons.Outlined.PermDeviceInformation,
                unselectedIcon = Icons.Outlined.PermDeviceInformation,
            ),

    NavItemType.Support to
            NavItem(
                title = R.string.support_contact_screen,
                screenRoute = Screens.Support.route,
                selectedIcon = Icons.Filled.Mail,
                unselectedIcon = Icons.Filled.Mail,
            ),
    )

// ------------------- Company Screen Info Items -------------------
val companyInfoScreenItemsMap = mapOf(
    NavItemType.CompanyWorkers to
        NavItem(
            title = R.string.company_workers_screen,
            screenRoute = Screens.Employees.route,
            selectedIcon = Icons.Outlined.Cases,
            unselectedIcon = Icons.Outlined.Cases,
        ),
    NavItemType.CompanyCustomers to
        NavItem(
            title = R.string.company_customers_screen,
            screenRoute = Screens.Customers.route,
            selectedIcon = Icons.Outlined.People,
            unselectedIcon = Icons.Outlined.People,
        ),
    NavItemType.CompanyAddress to
            NavItem(
                title = R.string.company_address_screen,
                screenRoute = null,
                selectedIcon = Icons.Outlined.LocationOn,
                unselectedIcon = Icons.Filled.AccountCircle,
            ),
)

// ------------------- Other screens -------------------

val otherNavigationItemsMap = mapOf(
    NavItemType.Profile to
        NavItem(
            title = R.string.profile_screen,
            screenRoute = Screens.Profile.route,
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Filled.AccountCircle,
        )
)

val allApplicationScreensMap =
    bottomNavigationBarNavItemsMap +
    moreBottomSheetItemsMap +
    companyInfoScreenItemsMap +
    otherNavigationItemsMap




enum class NavItemType {
    Home, Products, Orders, Company, More,
    Settings, FAQ, AboutApp, Support,
    CompanyAddress, CompanyWorkers, CompanyCustomers,
    Profile,
}