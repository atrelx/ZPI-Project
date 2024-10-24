package com.example.bussiness.ui.screens

sealed class Screens(val route : String) {
    data object Home : Screens("home_route")
    data object Products : Screens("products_route")
    data object Orders : Screens("orders_route")
    data object Company : Screens("company_route")


    data object Settings : Screens("settings_route")
    data object FAQ : Screens("faq_route")
    data object About : Screens("about_route")
    data object Support : Screens("support_route")

}