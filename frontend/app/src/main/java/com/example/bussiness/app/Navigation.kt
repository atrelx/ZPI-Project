package com.example.bussiness.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bussiness.ui.CustomSnackBar
import com.example.bussiness.ui.screens.Screens
import com.example.bussiness.ui.screens.bottom_screens.company.CompanyScreen
import com.example.bussiness.ui.screens.profile.ProfileScreen
import com.example.bussiness.ui.screens.bottom_screens.home.HomeScreen
import com.example.bussiness.ui.screens.bottom_screens.products.ProductScreen
import com.example.bussiness.ui.screens.bottom_screens.orders.OrdersScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.AboutScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.FAQScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.SettingsScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.SupportScreen
import com.example.bussiness.ui.screens.bottom_screens.company.address.CompanyAddressScreen
import com.example.bussiness.ui.screens.bottom_screens.company.customers.CompanyCustomersScreen
import com.example.bussiness.ui.screens.bottom_screens.company.CompanyScreenViewModel
import com.example.bussiness.ui.screens.bottom_screens.company.workers.CompanyWorkersScreen
import com.example.bussiness.ui.screens.bottom_screens.more_button.MoreBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomApplicationNavigation(appViewModel: AppViewModel = viewModel()) {
    val coroutineShape = rememberCoroutineScope()
    val appUiState by appViewModel.appUiState.collectAsState()
    val navigationController = rememberNavController()

    val currentRoute = navigationController.currentBackStackEntryAsState().value?.destination?.route

    val currentNavigationItem = allApplicationScreens.find { it.screen == currentRoute }

    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarIcon by remember { mutableStateOf<ImageVector?>(null) }

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

    fun callSnackBar(text: String, leadingIcon: ImageVector? = null) {
        snackbarIcon = leadingIcon
        coroutineShape.launch {
            snackbarHostState.showSnackbar(text)
            snackbarIcon = null
        }
    }

    val companyViewModel: CompanyScreenViewModel = viewModel()

    Scaffold(
        // -------------------- TOP BAR --------------------
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
                            IconButton(onClick = {
                                navigationController.navigate(otherNavigationItems[0].screen!!)
                            }) {
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
        // -------------------- BOTTOM NAVIGATION BAR --------------------
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
                                    contentDescription = stringResource(item.title),
                                )
                            }
                        )
                    }
                }
            }
        },
        // -------------------- CUSTOM SNACK BAR --------------------
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                CustomSnackBar(leadingIcon = snackbarIcon, it.visuals.message)
            }
        }
    ) { padding ->
        NavHost(navController = navigationController, startDestination = Screens.Home.route) {
            // -------------------- Bottom navigation screens --------------------
            composable(Screens.Home.route) { HomeScreen(navController = navigationController, padding) }
            composable(Screens.Products.route) { ProductScreen(navController = navigationController, padding) }
            composable(Screens.Orders.route) { OrdersScreen(navController = navigationController, padding) }
            composable(Screens.Company.route) { CompanyScreen(
                navController = navigationController,
                companyViewModel = companyViewModel,
                paddingValues = padding
            ) }
            // -------------------- Additional screens --------------------
            composable(Screens.Settings.route) { SettingsScreen(navController = navigationController, padding) }
            composable(Screens.FAQ.route) { FAQScreen(navController = navigationController, padding) }
            composable(Screens.About.route) { AboutScreen(navController = navigationController, padding) }
            composable(Screens.Support.route) { SupportScreen(navController = navigationController, padding) }

            // -------------------- Company info screens --------------------
            composable(Screens.CompanyAddress.route) { CompanyAddressScreen(
                navController = navigationController,
                companyViewModel = companyViewModel,
                paddingValues = padding,
                callSnackBar = { text, icon -> callSnackBar(text, icon)  },
            ) }
            composable(Screens.Workers.route) { CompanyWorkersScreen(
                navController = navigationController,
                companyViewModel = companyViewModel,
                paddingValues = padding,
                callSnackBar = { text, icon -> callSnackBar(text, icon) },
            ) }
            composable(Screens.Customers.route) { CompanyCustomersScreen(
                navController = navigationController,
                padding) }

            composable(Screens.Profile.route) { ProfileScreen(navigateToScreen = { navigateToScreen(it) }, padding) }
        }

        // -------------------- More Bottom Sheet --------------------
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