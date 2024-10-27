package com.example.bussiness.app

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bussiness.ui.screens.Screens
import com.example.bussiness.ui.screens.bottom_screens.company.CompanyScreen
import com.example.bussiness.ui.screens.bottom_screens.profile.ProfileScreen
import com.example.bussiness.ui.screens.bottom_screens.home.HomeScreen
import com.example.bussiness.ui.screens.bottom_screens.products.ProductScreen
import com.example.bussiness.ui.screens.bottom_screens.orders.OrdersScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.AboutScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.FAQScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.SettingsScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.SupportScreen
import com.example.bussiness.ui.screens.bottom_screens.company.CompanyAddressScreen
import com.example.bussiness.ui.screens.bottom_screens.company.CompanyCustomersScreen
import com.example.bussiness.ui.screens.bottom_screens.company.CompanyWorkersScreen
import com.example.bussiness.ui.screens.bottom_screens.more_button.MoreBottomSheet

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomApplicationNavigation(appViewModel: AppViewModel = viewModel()) {
    val appUiState by appViewModel.appUiState.collectAsState()
    val navigationController = rememberNavController()

    val currentRoute = navigationController.currentBackStackEntryAsState().value?.destination?.route

    val currentNavigationItem = allApplicationScreens.find { it.screen == currentRoute }

    if (currentNavigationItem != null) {
        appViewModel.updateCurrentNavItem(currentNavigationItem)
    }

    fun navigateToScreen(navigationItem: NavigationItem) {
        navigationItem.screen?.let {
            navigationController.navigate(it) {
                popUpTo(Screens.Home.route) {
                    inclusive = false
                    saveState = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
                val scrollBehavior =
                    TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(appUiState.currentNavigationItem.title), // What must be here?
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        if (!appUiState.appNavigationVisibility) {
                            IconButton(onClick = { navigationController.navigateUp() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    actions = {
                        if (appUiState.appNavigationVisibility) {
                            IconButton(onClick = { navigateToScreen(otherNavigationItems[0] ) }) {
                                Icon(
                                    imageVector = otherNavigationItems[0].selectedIcon,
                                    contentDescription = null)
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
                                    navigateToScreen(item)
                                } ?: run {
                                    appViewModel.updateMoreBottomSheetVisibility(true)
                                }
                            },
                            label = { Text(text = stringResource(item.title)) },
                            alwaysShowLabel = true,
                            icon = {
                                Icon(
                                    imageVector = if (appUiState.currentNavigationItem == item) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = stringResource(item.title)
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
            composable(Screens.Products.route) { ProductScreen(navController = navigationController, padding) }
            composable(Screens.Orders.route) { OrdersScreen(navController = navigationController, padding) }
            composable(Screens.Company.route) { CompanyScreen(navController = navigationController, padding) }

            composable(Screens.Settings.route) { SettingsScreen(navController = navigationController, padding) }
            composable(Screens.FAQ.route) { FAQScreen(navController = navigationController, padding) }
            composable(Screens.About.route) { AboutScreen(navController = navigationController, padding) }
            composable(Screens.Support.route) { SupportScreen(navController = navigationController, padding) }

            composable(Screens.CompanyAddress.route) { CompanyAddressScreen(navController = navigationController, padding) }
            composable(Screens.Workers.route) { CompanyWorkersScreen(navController = navigationController, padding) }
            composable(Screens.Customers.route) { CompanyCustomersScreen(navController = navigationController, padding) }

            composable(Screens.Profile.route) { ProfileScreen(navigateToScreen = { navigateToScreen(it) }, padding) }
        }

        if (appUiState.moreBottomSheetIsVisible) {
            MoreBottomSheet(
                hideMoreBottomSheet = { appViewModel.updateMoreBottomSheetVisibility(false) },
                onClick = { navItem ->
                    appViewModel.updateMoreBottomSheetVisibility(false)
                    navigateToScreen(navItem)
                }
            )
        }

        BackHandler {
            navigationController.navigateUp()
        }
    }
}
