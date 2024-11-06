package com.example.bussiness.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bussiness.ui.screens.Screens
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.AboutScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.FAQScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.SettingsScreen
import com.example.bussiness.ui.screens.bottom_screens.additional_screens.SupportScreen
import com.example.bussiness.ui.screens.bottom_screens.company.CompanyScreen
import com.example.bussiness.ui.screens.bottom_screens.company.CompanyScreenViewModel
import com.example.bussiness.ui.screens.bottom_screens.company.customers.CompanyCustomersScreen
import com.example.bussiness.ui.screens.bottom_screens.company.employees.CompanyEmployeesScreen
import com.example.bussiness.ui.screens.bottom_screens.home.HomeScreen
import com.example.bussiness.ui.screens.bottom_screens.orders.OrdersScreen
import com.example.bussiness.ui.screens.bottom_screens.products.ProductScreen
import com.example.bussiness.ui.screens.profile.ProfileScreen

@Composable
fun AppNavigationHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    callSnackBar: (String, ImageVector?) -> Unit
) {
    val companyViewModel: CompanyScreenViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screens.Home.route) {

        // -------------------- Bottom navigation screens --------------------
        composable(Screens.Home.route) {
            HomeScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }
        composable(Screens.Products.route) {
            ProductScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }
        composable(Screens.Orders.route) {
            OrdersScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }
        composable(Screens.Company.route) {
            CompanyScreen(
                navController = navController,
                companyViewModel = companyViewModel,
                paddingValues = paddingValues
            ) }

        // -------------------- 'More' bottom sheet screens --------------------
        composable(Screens.Settings.route) {
            SettingsScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }
        composable(Screens.FAQ.route) {
            FAQScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }
        composable(Screens.About.route) {
            AboutScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }
        composable(Screens.Support.route) {
            SupportScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }

        // -------------------- Company info screens --------------------

        composable(Screens.Employees.route) {
            CompanyEmployeesScreen(
                navController = navController,
                companyViewModel = companyViewModel,
                paddingValues = paddingValues,
                callSnackBar = { text, icon -> callSnackBar(text, icon) },
            ) }
        composable(Screens.Customers.route) {
            CompanyCustomersScreen(
                navController = navController,
                companyViewModel = companyViewModel,
                paddingValues = paddingValues,
                callSnackBar = { text, icon -> callSnackBar(text, icon) },
            ) }

        // -------------------- Profile Screen --------------------
        composable(Screens.Profile.route) {
            ProfileScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }
    }
}