package com.example.bussiness.screens

sealed class Screens(val route : String) {
    data object Home : Screens("home_route")
    data object Selling : Screens("selling_route")
    data object Expanses : Screens("expanses_route")
    data object Statistics : Screens("statistics_route")

    data object Products : Screens("products_route")
    data object Expanses_Templates : Screens("expanses_templates_route")
    data object Settings : Screens("settings_route")
    data object FAQ : Screens("faq_route")
    data object About : Screens("about_route")
    data object Support : Screens("support_route")

}