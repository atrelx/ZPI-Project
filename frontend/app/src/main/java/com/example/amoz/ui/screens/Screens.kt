package com.example.amoz.ui.screens

sealed class Screens(val route : String) {
    // --------------------- Bottom Navigation Bar Screens ---------------------
    data object Home : Screens("home_route")
    data object Products : Screens("products_route")
    data object Orders : Screens("orders_route")
    data object Company : Screens("company_route")

    // --------------------- Products Screen ---------------------
    data object Categories : Screens("products_categories_route")
    data object StockDelivery : Screens("products_stock_delivery_route")

    // --------------------- 'More' Button Screens ---------------------
    data object Settings : Screens("settings_route")
    data object FAQ : Screens("faq_route")
    data object About : Screens("about_route")
    data object Support : Screens("support_route")

    // --------------------- Other Bar Screens ---------------------
    data object Profile : Screens("profile_route")
    // --------------------- Company Info Screens ---------------------
    data object Employees : Screens("employees_route")
    data object Customers : Screens("customers_route")

}