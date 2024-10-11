package com.example.bussiness.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bussiness.screens.Screens

data class NavigationItem(
    val title: String,
    val screen: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val bottom_items = listOf(
    NavigationItem(
        title = "Home",
        screen = Screens.Home.route,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    NavigationItem(
        title = "Selling",
        screen = Screens.Selling.route,
        selectedIcon = Icons.Filled.AttachMoney,
        unselectedIcon = Icons.Outlined.AttachMoney,
    ),
    NavigationItem(
        title = "Expanses",
        screen = Screens.Expanses.route,
        selectedIcon = Icons.Filled.Payments,
        unselectedIcon = Icons.Outlined.Payments,
    ),
    NavigationItem(
        title = "Statistics",
        screen = Screens.Statistics.route,
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart,
    ),
)

val drawer_items = listOf(
    NavigationItem(
        title = "Products",
        screen = Screens.Products.route,
        selectedIcon = Icons.Filled.Category,
        unselectedIcon = Icons.Outlined.Category,
    ),
    NavigationItem(
        title = "Expanses",
        screen = Screens.Expanses_Templates.route,
        selectedIcon = Icons.Filled.Payments,
        unselectedIcon = Icons.Outlined.Payments,
    ),
    NavigationItem(
        title = "Settings",
        screen = Screens.Settings.route,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    ),
    NavigationItem(
        title = "FAQ",
        screen = Screens.FAQ.route,
        selectedIcon = Icons.Filled.QuestionAnswer,
        unselectedIcon = Icons.Outlined.QuestionAnswer,
    ),
    NavigationItem(
        title = "About App",
        screen = Screens.About.route,
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
    ),
    NavigationItem(
        title = "Support",
        screen = Screens.Support.route,
        selectedIcon = Icons.Filled.Help,
        unselectedIcon = Icons.Outlined.Help,
    ),
)