package com.example.bussiness.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bussiness.screens.Screens
import com.example.bussiness.screens.bottom_screens.ExpansesScreen
import com.example.bussiness.screens.bottom_screens.HomeScreen
import com.example.bussiness.screens.bottom_screens.selling.SellingScreen
import com.example.bussiness.screens.bottom_screens.StatisticsScreen
import com.example.bussiness.screens.drawer_screens.AboutScreen
import com.example.bussiness.screens.drawer_screens.expanses.ExpansesTemplatesScreen
import com.example.bussiness.screens.drawer_screens.FAQScreen
import com.example.bussiness.screens.drawer_screens.products.ProductScreen
import com.example.bussiness.screens.drawer_screens.SettingsScreen
import com.example.bussiness.screens.drawer_screens.SupportScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationSheet() {
    val navigationController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    var selectedItemIndexDrawer by rememberSaveable { mutableStateOf(-1) }
    var bottomNavigationVisibility by remember { mutableStateOf(true) }
    var popUpped by remember { mutableStateOf(false) }

    fun popBack() {
        Log.d("---------------------", "Pop backed")
        selectedItemIndexDrawer = -1
        bottomNavigationVisibility = true
        navigationController.navigateUp()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true, // Disable swipe gesture to open drawer
        drawerContent = {
            ModalDrawerSheet {
                Text (
                    text = "Business Orders",
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(28.dp)
                )
                drawer_items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndexDrawer,
                        onClick = {
                            navigationController.navigate(item.screen) {
                                popUpTo(bottom_items[selectedItemIndex].screen) {
                                    inclusive = false
                                    saveState = false
                                }
                            }
                            selectedItemIndexDrawer = index
                            coroutineScope.launch { drawerState.close() }
                            bottomNavigationVisibility = false
                            popUpped = true
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndexDrawer) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    if (index == 2) {
                        Box (modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)) {
                            Divider(modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding))
                            Text(text = "Application",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(12.dp))
                        }

                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Main screen",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(),
                    navigationIcon = {
                        if (selectedItemIndexDrawer == -1) {
                            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                                Icon(imageVector = Icons.Outlined.Menu, contentDescription = "Menu")
                            }
                        } else {
                            IconButton(onClick = { coroutineScope.launch { popBack() } }) {
                                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                if (bottomNavigationVisibility) {
                    NavigationBar {
                        bottom_items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index
                                    navigationController.navigate(item.screen) {
                                        popUpTo(Screens.Home.route) {
                                            inclusive = false
                                            saveState = true
                                        }
                                    }
                                },
                                label = { Text(text = item.title) },
                                alwaysShowLabel = false,
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
                composable(Screens.Expanses_Templates.route) { ExpansesTemplatesScreen(navController = navigationController, padding) }
                composable(Screens.Settings.route) { SettingsScreen(navController = navigationController, padding) }
                composable(Screens.FAQ.route) { FAQScreen(navController = navigationController, padding) }
                composable(Screens.About.route) { AboutScreen(navController = navigationController, padding) }
                composable(Screens.Support.route) { SupportScreen(navController = navigationController, padding) }
            }
            BackHandler(popUpped) {
                popUpped = false
                coroutineScope.launch { popBack() }
            }
        }
    }
}
