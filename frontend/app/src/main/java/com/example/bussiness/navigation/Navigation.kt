package com.example.bussiness.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bussiness.ui.screens.Screens
import com.example.bussiness.ui.screens.bottom_screens.ExpansesScreen
import com.example.bussiness.ui.screens.bottom_screens.HomeScreen
import com.example.bussiness.ui.screens.bottom_screens.StatisticsScreen
import com.example.bussiness.ui.screens.bottom_screens.products.ProductScreen
import com.example.bussiness.ui.screens.bottom_screens.selling.SellingScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.AboutScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.FAQScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.SettingsScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.SupportScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomApplicationNavigation() {
    val navigationController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val bottomNavigationVisibility by remember { mutableStateOf(true) }
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    var moreBottomSheetIsVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = bottomNavigationBarItems[selectedItemIndex].title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            if (bottomNavigationVisibility) {
                NavigationBar {
                    bottomNavigationBarItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                item.screen?.let {
                                    selectedItemIndex = index
                                    navigationController.navigate(it) {
                                        popUpTo(Screens.Home.route) {
                                            inclusive = false
                                            saveState = true
                                        }
                                    }
                                } ?: run {
                                    moreBottomSheetIsVisible = true
                                }
                            },
                            label = { Text(text = item.title) },
                            alwaysShowLabel = true,
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(navController = navigationController, startDestination = Screens.Home.route) {
            composable(Screens.Home.route) { HomeScreen(navController = navigationController, padding) }
            composable(Screens.Selling.route) { SellingScreen(navController = navigationController, padding) }
            composable(Screens.Expanses.route) { ExpansesScreen(navController = navigationController, padding) }
            composable(Screens.Statistics.route) { StatisticsScreen(navController = navigationController, padding) }
            composable(Screens.Products.route) { ProductScreen(navController = navigationController, padding) }
            composable(Screens.Settings.route) { SettingsScreen(navController = navigationController, padding) }
            composable(Screens.FAQ.route) { FAQScreen(navController = navigationController, padding) }
            composable(Screens.About.route) { AboutScreen(navController = navigationController, padding) }
            composable(Screens.Support.route) { SupportScreen(navController = navigationController, padding) }
        }
        if (moreBottomSheetIsVisible) {
            MoreBottomSheet(
                hideMoreBottomSheet = { moreBottomSheetIsVisible = false },
                navigationController = navigationController,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreBottomSheet(
    hideMoreBottomSheet: () -> Unit,
    navigationController: NavHostController,
) {
    ModalBottomSheet(
        onDismissRequest = { hideMoreBottomSheet() }
    )
    {
        Column {
            moreBottomSheetItems.forEach { item ->
                BottomSheetNavigationRaw(
                    icon = item.selectedIcon,
                    text = item.title,
                    onClick = {
                        hideMoreBottomSheet()
                        navigationController.navigate(item.screen!!)
                    }
                )
            }
        }
    }
}
@Composable
fun BottomSheetNavigationRaw(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit 
) {
    ListItem(
        modifier = Modifier
            .clickable(onClick = onClick),
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )},
        headlineContent = { Text(text = text) },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    )
}