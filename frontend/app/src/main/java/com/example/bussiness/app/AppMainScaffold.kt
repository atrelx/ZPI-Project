package com.example.bussiness.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bussiness.data.NavItem
import com.example.bussiness.ui.CustomSnackBar
import com.example.bussiness.ui.screens.Screens
import com.example.bussiness.ui.screens.bottom_screens.more_button.MoreBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMainScaffold(
    appViewModel: AppViewModel = viewModel(),
    bottomNavigationItems: List<NavItem> = bottomNavigationBarNavItemsMap.values.toList()
) {
    val appUiState by appViewModel.appUiState.collectAsState()

    val navigationController = rememberNavController()

    val currentRoute = navigationController.currentBackStackEntryAsState().value?.destination?.route
    val currentNavigationItem = allApplicationScreensMap.values.find { it.screenRoute == currentRoute }
    if (currentNavigationItem != null) {
        appViewModel.updateCurrentNavItem(currentNavigationItem)
    }

    fun navigateToScreen(navigationItem: NavItem) {
        if (currentNavigationItem != navigationItem) {
            navigationItem.screenRoute?.let {
                navigationController.navigate(it) {
                    popUpTo(Screens.Home.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    val coroutineShape = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarIcon by remember { mutableStateOf<ImageVector?>(null) }

    fun callSnackBar(text: String, leadingIcon: ImageVector? = null) {
        snackBarIcon = leadingIcon
        coroutineShape.launch {
            snackBarHostState.showSnackbar(text)
            snackBarIcon = null
        }
    }

    Scaffold(
        // -------------------- TOP BAR --------------------
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = appUiState.currentNavigationItem?.let
                        { stringResource(it.title) } ?: "",
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
                        IconButton(
                            onClick = {
                                navigationController.navigate(
                                    otherNavigationItemsMap[NavItemType.Profile]!!.screenRoute!!
                                )
                            }) {
                            Icon(
                                imageVector = otherNavigationItemsMap[NavItemType.Profile]!!.selectedIcon,
                                contentDescription = null)
                        }
                    }
                },
            )
        },
        // -------------------- BOTTOM NAVIGATION BAR --------------------
        bottomBar = {
            if (appUiState.appNavigationVisibility) {
                NavigationBar {
                    bottomNavigationItems.forEach { item ->
                        NavigationBarItem(
                            selected = appUiState.currentNavigationItem == item,
                            onClick = {
                                item.screenRoute?.let {
                                    navigateToScreen(item)
                                } ?: run {
                                    appViewModel.updateMoreBottomSheetVisibility(true)
                                }
                            },
                            label = { Text(text = stringResource(item.title)) },
                            alwaysShowLabel = true,
                            icon = {
                                Icon(
                                    imageVector =
                                        if (appUiState.currentNavigationItem == item) {
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
        // -------------------- Custom Snack Bar --------------------
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                CustomSnackBar(
                    message = it.visuals.message,
                    leadingIcon = snackBarIcon
                )
            }
        }
    ) { paddingValues ->
        // -------------------- App NavHost --------------------
        AppNavigationHost(
            navController = navigationController,
            paddingValues = paddingValues,
            callSnackBar = { text, icon -> callSnackBar(text = text, leadingIcon = icon) }
        )

        // -------------------- 'More' Bottom Sheet --------------------
        if (appUiState.moreBottomSheetIsVisible) {
            MoreBottomSheet(
                hideMoreBottomSheet = { appViewModel.updateMoreBottomSheetVisibility(false) },
                navigateToScreen = { navItem -> navigateToScreen(navItem) }
            )
        }
    }
}