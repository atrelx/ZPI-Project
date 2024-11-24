package com.example.amoz.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Cases
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PermDeviceInformation
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.WorkOutline
import com.example.amoz.R
import com.example.amoz.data.NavItem
import com.example.amoz.ui.screens.Screens


// --------------------- Bottom Navigation Bar Items ---------------------

val bottomNavigationBarNavItemsMap = mapOf(
    NavItemType.Home to
            NavItem(
                title = R.string.home_screen,
                screenRoute = Screens.Home.route,
                icon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),

    NavItemType.Products to
            NavItem(
                title = R.string.products_screen,
                screenRoute = Screens.Products.route,
                icon = Icons.Filled.Sell,
                unselectedIcon = Icons.Outlined.Sell,
            ),

    NavItemType.Orders to
            NavItem(
                title = R.string.orders_screen,
                screenRoute = Screens.Orders.route,
                icon = Icons.Filled.ShoppingCart,
                unselectedIcon = Icons.Outlined.ShoppingCart,
            ),

    NavItemType.Company to
            NavItem(
                title = R.string.company_screen,
                screenRoute = Screens.Company.route,
                icon = Icons.Filled.Work,
                unselectedIcon = Icons.Outlined.WorkOutline,
            )
)

// ------------------- Products Screen Bottom Sheet Menu -------------------
val productScreenBottomSheetMenu = mapOf(
    NavItemType.Categories to
            NavItem(
                title = R.string.products_categories_screen,
                screenRoute = Screens.Categories.route,
                icon = Icons.Filled.Category,
            ),
    NavItemType.Attributes to
            NavItem(
                title = R.string.products_attributes_screen,
                screenRoute = Screens.Attributes.route,
                icon = Icons.Filled.CheckBox,
            ),
    NavItemType.StockDelivery to
            NavItem(
                title = R.string.products_stock_delivery_screen,
                screenRoute = Screens.StockDelivery.route,
                icon = Icons.Filled.LocalShipping,
            )
)

// --------------------- 'More' Button Bar Items ---------------------

val moreBottomSheetItemsMap = mapOf(
    NavItemType.Settings to NavItem(
        title = R.string.settings_screen,
        screenRoute = Screens.Settings.route,
        icon = Icons.Filled.Settings
    ),

    NavItemType.FAQ to NavItem(
        title = R.string.faq_screen,
        screenRoute = Screens.FAQ.route,
        icon = Icons.Filled.Info
    ),

    NavItemType.AboutApp to NavItem(
        title = R.string.about_screen,
        screenRoute = Screens.About.route,
        icon = Icons.Outlined.PermDeviceInformation
    ),

    NavItemType.Support to NavItem(
        title = R.string.support_contact_screen,
        screenRoute = Screens.Support.route,
        icon = Icons.Filled.Mail
    )
)


// ------------------- Company Screen Info Items -------------------
val companyInfoScreenItemsMap = mapOf(
    NavItemType.CompanyWorkers to
        NavItem(
            title = R.string.company_workers_screen,
            screenRoute = Screens.Employees.route,
            icon = Icons.Outlined.Cases,
            unselectedIcon = Icons.Outlined.Cases,
        ),
    NavItemType.CompanyCustomers to
        NavItem(
            title = R.string.company_customers_screen,
            screenRoute = Screens.Customers.route,
            icon = Icons.Outlined.People,
            unselectedIcon = Icons.Outlined.People,
        )
)

// ------------------- Other screens -------------------

val otherNavigationItemsMap = mapOf(
    NavItemType.Profile to
        NavItem(
            title = R.string.profile_screen,
            screenRoute = Screens.Profile.route,
            icon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Filled.AccountCircle,
        )
)

val allApplicationScreensMap =
    bottomNavigationBarNavItemsMap +
    productScreenBottomSheetMenu +
    moreBottomSheetItemsMap +
    companyInfoScreenItemsMap +
    otherNavigationItemsMap



enum class NavItemType {
    // Each item within special list writes on new line
    Home, Products, Orders, Company, More,
    AddSimpleProduct, AddProductVariant, AddProductTemplate, Categories, Attributes, StockDelivery,
    CompanyAddress, CompanyWorkers, CompanyCustomers,
    Settings, FAQ, AboutApp, Support,
    Profile,
}