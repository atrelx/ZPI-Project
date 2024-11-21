package com.example.amoz.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.amoz.data.NavItem
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.screens.bottom_screens.additional_screens.AboutScreen
import com.example.amoz.ui.screens.bottom_screens.additional_screens.FAQScreen
import com.example.amoz.ui.screens.bottom_screens.additional_screens.SettingsScreen
import com.example.amoz.ui.screens.bottom_screens.additional_screens.SupportScreen
import com.example.amoz.ui.screens.bottom_screens.company.CompanyScreen
import com.example.amoz.ui.screens.bottom_screens.company.CompanyScreenViewModel
import com.example.amoz.ui.screens.bottom_screens.company.customers.CompanyCustomersScreen
//import com.example.amoz.ui.screens.bottom_screens.company.employees.CompanyEmployeesScreen
import com.example.amoz.ui.screens.bottom_screens.home.HomeScreen
import com.example.amoz.ui.screens.bottom_screens.orders.OrdersScreen
import com.example.amoz.ui.screens.bottom_screens.products.ProductScreen
import com.example.amoz.ui.screens.bottom_screens.attributes.ProductsAttributes
import com.example.amoz.ui.screens.bottom_screens.categories.CategoriesScreen
import com.example.amoz.ui.screens.bottom_screens.company.employees.CompanyEmployeesScreen
import com.example.amoz.ui.screens.bottom_screens.delivery_stock.ProductsStockDelivery
import com.example.amoz.ui.screens.profile.ProfileScreen

@Composable
fun AppNavigationHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    navigateToScreen: (NavItem) -> Unit,
    callSnackBar: (String, ImageVector?) -> Unit
) {
    val companyViewModel: CompanyScreenViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screens.Home.route) {

        // -------------------- Bottom navigation screens --------------------
        composable(Screens.Home.route) {
            HomeScreen(
                navigateToScreen = { navigateToScreen(it) },
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

        // -------------------- Products screens --------------------
//        composable(Screens.AddEditProduct.route) {
//            ProductAddEditScreen(
//                navController = navController,
//                paddingValues = paddingValues,
//                product = Product(),
//                onComplete = {},
////                onImageUpload = {}
//            ) }

        composable(Screens.Categories.route) {
            CategoriesScreen(
                paddingValues = paddingValues,
            ) }

        composable(Screens.Attributes.route) {
            ProductsAttributes(
                paddingValues = paddingValues,
            ) }

        composable(Screens.StockDelivery.route) {
            ProductsStockDelivery(
                paddingValues = paddingValues,
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
            )
        }
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