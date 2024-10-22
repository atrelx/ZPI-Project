package com.example.bussiness.app

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.bussiness.ui.screens.bottom_screens.more_button.MoreBottomSheet
import com.example.bussiness.ui.screens.bottom_screens.products.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomApplicationNavigation(
    appViewModel: AppViewModel = viewModel()
    ) {
        val appUiState by appViewModel.appUiState.collectAsState()
        val navigationController = rememberNavController()
        fun popBackHandler() {
            appViewModel.updateCurrentNavItem()
            navigationController.navigateUp()
        }
        Scaffold(
            topBar = {

                    val scrollBehavior =
                        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = appUiState.currentNavigationItem.title, // What must be here?
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            if (!appUiState.appNavigationVisibility) {
                                IconButton(onClick = { popBackHandler() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        actions = {
                            if (appUiState.appNavigationVisibility) {
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(Icons.Filled.AccountCircle, contentDescription = null)
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(),
                        scrollBehavior = scrollBehavior
                    )
                },
            bottomBar = {
                if (appUiState.appNavigationVisibility) {
                    NavigationBar {
                        bottomNavigationBarItems.forEach { item ->
                            NavigationBarItem(
                                selected = appUiState.currentNavigationItem == item,
                                onClick = {
                                    item.screen?.let {
                                        appViewModel.updateCurrentNavItem(item)
                                        navigationController.navigate(it) {
                                            popUpTo(Screens.Home.route) {
                                                inclusive = false
                                                saveState = true
                                            }
                                        }
                                    } ?: run {
                                        appViewModel.updateMoreBottomSheetVisibility(true)
                                    }
                                },
                                label = { Text(text = item.title) },
                                alwaysShowLabel = true,
                                icon = {
                                    Icon(
                                        imageVector = if (appUiState.currentNavigationItem == item) {
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
            if (appUiState.moreBottomSheetIsVisible) {
                MoreBottomSheet(
                    hideMoreBottomSheet = { appViewModel.updateMoreBottomSheetVisibility(false) },
                    onClick = { navItem ->
                        appViewModel.updateCurrentNavItem(navItem)
                        appViewModel.updateMoreBottomSheetVisibility(false)
                        navigationController.navigate(navItem.screen!!) {
                            popUpTo(Screens.Home.route) {
                                inclusive = false
                                saveState = true
                            }
                        }
                    }
                )
            }

            BackHandler {
                popBackHandler()
            }
        }
    }
