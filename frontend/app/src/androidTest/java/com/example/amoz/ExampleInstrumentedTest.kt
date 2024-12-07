//package com.example.bussiness
//
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.assertTextEquals
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.ComposeNavigator
//import androidx.navigation.compose.composable
//import androidx.navigation.createGraph
//import androidx.navigation.testing.TestNavHostController
//import androidx.test.espresso.IdlingRegistry
//import androidx.test.espresso.idling.CountingIdlingResource
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.LargeTest
//import com.example.bussiness.screens.Screens
//import com.example.bussiness.screens.bottom_screens.HomeScreen
//import com.example.bussiness.screens.bottom_screens.SalesAddEditVIew
//import com.example.bussiness.screens.bottom_screens.SoldProduct
//import com.example.bussiness.screens.drawer_screens.ProductScreen
//import com.google.firebase.Firebase
//import com.google.firebase.FirebaseApp
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.database
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import kotlin.system.measureTimeMillis
//import androidx.test.platform.app.InstrumentationRegistry
//import com.example.bussiness.firebase.addOrUpdateSaleInFirebase
//import com.example.bussiness.firebase.deleteSaleFromFirebase
//import com.example.bussiness.firebase.getSalesFromFirebase
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.storage.ktx.storage
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertTrue
//import java.util.Calendar
//import java.util.UUID
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
//
//@Composable
//fun TestNavController(): NavHostController {
//    val context = LocalContext.current
//    val navController = TestNavHostController(context)
//
//    navController.navigatorProvider.addNavigator(ComposeNavigator())
//    navController.graph = navController.createGraph(startDestination = Screens.Home.route) {
//        composable(Screens.Home.route) { HomeScreen(navController = navController, paddingValues = PaddingValues()) }
//        composable(Screens.Selling.route) { /* Mock SellingScreen for tests */ }
//        composable(Screens.Expanses.route) { /* Mock ExpansesScreen for tests */ }
//        composable(Screens.Statistics.route) { /* Mock StatisticsScreen for tests */ }
//        composable(Screens.Products.route) { /* Mock ProductScreen for tests */ }
//        composable(Screens.Expanses_Templates.route) { /* Mock ExpansesTemplatesScreen for tests */ }
//        composable(Screens.Settings.route) { /* Mock SettingsScreen for tests */ }
//        composable(Screens.FAQ.route) { /* Mock FAQScreen for tests */ }
//        composable(Screens.About.route) { /* Mock AboutScreen for tests */ }
//        composable(Screens.Support.route) { /* Mock SupportScreen for tests */ }
//    }
//
//    return navController
//}
//
//@RunWith(AndroidJUnit4::class)
//@LargeTest
//class HomeScreenTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun testHomeScreenDisplaysFab() {
//        // Start the HomeScreen Composable
//        composeTestRule.setContent {
//            HomeScreen(navController = TestNavController(), paddingValues = PaddingValues())
//        }
//
//        // Check if the FAB is displayed using unmerged tree
//        composeTestRule.onNodeWithText("New Product", useUnmergedTree = true).assertIsDisplayed()
//    }
//
//
//    @Test
//    fun testFabOpensDialog() {
//        composeTestRule.setContent {
//            HomeScreen(navController = TestNavController(), paddingValues = PaddingValues())
//        }
//
//        // Perform click on the FAB using unmerged tree
//        composeTestRule.onNodeWithText("New Product", useUnmergedTree = true).performClick()
//
//        // Check if the dialog is displayed by looking for the "Add Product" text in the unmerged tree
//        composeTestRule.onNodeWithText("Add Product", useUnmergedTree = true).assertIsDisplayed()
//    }
//
//
//    @Test
//    fun testListItemClickOpensDialog() {
//        composeTestRule.setContent {
//            HomeScreen(navController = TestNavController(), paddingValues = PaddingValues())
//        }
//
//        // Wait for a few seconds to ensure the data is loaded
//        Thread.sleep(3000)
//
//        // Use the test tag to find the specific product item and perform a click
//        composeTestRule.onNodeWithTag("ProductItem_3bb5c30f-19eb-48ed-9702-1ac7906b69a4", useUnmergedTree = true).performClick()
//
//        // Check if the dialog is displayed by looking for the "Update Product" text
//        composeTestRule.onNodeWithText("Update Product", useUnmergedTree = true).assertIsDisplayed()
//    }
//}
//
//
//@RunWith(AndroidJUnit4::class)
//@LargeTest
//class SalesAddEditViewTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun testAddProductDialog() {
//        composeTestRule.setContent {
//            SalesAddEditVIew(saleProduct = null, setShowDialog = {})
//        }
//
//        // Wait for the dialog to appear
//        composeTestRule.onNodeWithText("Add Product", useUnmergedTree = true).assertIsDisplayed()
//
//        // Open the dropdown menu by clicking on the product name field
//        composeTestRule.onNodeWithTag("productName", useUnmergedTree = true).performClick()
//
//        Thread.sleep(2000)
//
//        // Select an item from the dropdown list using the testTag
//        composeTestRule.onNodeWithTag("DropdownItem_Bukiet_z_motyli,_15", useUnmergedTree = true).performClick()
//
//        // Check that the fields are populated correctly
//        composeTestRule.onNodeWithTag("pricePerItem", useUnmergedTree = true).assertTextEquals("120.0")
//        composeTestRule.onNodeWithTag("amount", useUnmergedTree = true).assertTextEquals("1")
//        composeTestRule.onNodeWithTag("totalPrice", useUnmergedTree = true).assertTextEquals("120.0")
//        composeTestRule.onNodeWithTag("saleDate", useUnmergedTree = true).assertTextEquals("19-06-2024")
//    }
//
//    @Test
//    fun testUpdateProductDialog() {
//        val testProduct = SoldProduct(
//            id = "1",
//            name = "Bukiet z motyli, 15",
//            salePrice = 120.0,
//            saleDate = System.currentTimeMillis(),
//            imageUrl = "",
//            amount = 1,
//            totalPrice = 120.0,
//            characteristics = mapOf("Podswi€tlenie" to 0.0, "Kolor" to 0.0)
//        )
//
//        composeTestRule.setContent {
//            SalesAddEditVIew(saleProduct = testProduct, setShowDialog = {})
//        }
//
//        // Wait for the dialog to appear
//        composeTestRule.onNodeWithText("Update Product", useUnmergedTree = true).assertIsDisplayed()
//
//        // Check that the fields are pre-filled correctly
//        composeTestRule.onNodeWithTag("productName", useUnmergedTree = true).assertTextEquals("Bukiet z motyli, 15")
//        composeTestRule.onNodeWithTag("pricePerItem", useUnmergedTree = true).assertTextEquals("120.0")
//        composeTestRule.onNodeWithTag("amount", useUnmergedTree = true).assertTextEquals("1")
//        composeTestRule.onNodeWithTag("totalPrice", useUnmergedTree = true).assertTextEquals("120.0")
//        composeTestRule.onNodeWithTag("saleDate", useUnmergedTree = true).assertTextEquals("19-06-2024")
//    }
//}
//
//// ------------------------------------------------------------------
//
//@RunWith(AndroidJUnit4::class)
//@LargeTest
//class FirebaseSalesTest {
//
//    private lateinit var firebaseDatabase: FirebaseDatabase
//
//    @Before
//    fun setUp() {
//        firebaseDatabase = FirebaseDatabase.getInstance()
//    }
//
//    @Test
//    fun testFetchSalesFromFirebase() = runBlocking<Unit> {
//        val salesList = mutableListOf<SoldProduct>()
//        getSalesFromFirebase { sales ->
//            salesList.addAll(sales)
//        }
//
//        // Подождем, чтобы данные успели загрузиться
//        Thread.sleep(3000)
//
//        // Проверим, что список продаж не пуст
//        assertTrue(salesList.isNotEmpty())
//    }
//
//    @Test
//    fun testAddSaleToFirebase() = runBlocking<Unit> {
//        val saleId = UUID.randomUUID().toString()
//        val newSale = SoldProduct(
//            id = saleId,
//            name = "Test Product",
//            salePrice = 100.0,
//            saleDate = Calendar.getInstance().timeInMillis,
//            imageUrl = "",
//            amount = 1,
//            totalPrice = 100.0,
//            characteristics = mapOf("Feature" to "Value")
//        )
//
//        addOrUpdateSaleInFirebase(newSale)
//
//        // Подождем, чтобы данные успели загрузиться
//        Thread.sleep(3000)
//
//        firebaseDatabase.getReference("sales/$saleId").get().addOnSuccessListener { dataSnapshot ->
//            val fetchedSale = dataSnapshot.getValue(SoldProduct::class.java)
//            assertEquals(newSale.name, fetchedSale?.name)
//        }
//    }
//
//    @Test
//    fun testUpdateSaleInFirebase() = runBlocking<Unit> {
//        val saleId = UUID.randomUUID().toString()
//        val newSale = SoldProduct(
//            id = saleId,
//            name = "Initial Product",
//            salePrice = 50.0,
//            saleDate = Calendar.getInstance().timeInMillis,
//            imageUrl = "",
//            amount = 1,
//            totalPrice = 50.0,
//            characteristics = mapOf("Feature" to "Value")
//        )
//
//        addOrUpdateSaleInFirebase(newSale)
//
//        // Подождем, чтобы данные успели загрузиться
//        Thread.sleep(3000)
//
//        val updatedSale = newSale.copy(name = "Updated Product", salePrice = 75.0)
//        addOrUpdateSaleInFirebase(updatedSale)
//
//        // Подождем, чтобы данные успели загрузиться
//        Thread.sleep(3000)
//
//        firebaseDatabase.getReference("sales/$saleId").get().addOnSuccessListener { dataSnapshot ->
//            val fetchedSale = dataSnapshot.getValue(SoldProduct::class.java)
//            assertEquals(updatedSale.name, fetchedSale?.name)
//            assertEquals(updatedSale.salePrice, fetchedSale?.salePrice)
//        }
//    }
//
//    @Test
//    fun testDeleteSaleFromFirebase() = runBlocking<Unit> {
//        val saleId = UUID.randomUUID().toString()
//        val newSale = SoldProduct(
//            id = saleId,
//            name = "Product to Delete",
//            salePrice = 30.0,
//            saleDate = Calendar.getInstance().timeInMillis,
//            imageUrl = "",
//            amount = 1,
//            totalPrice = 30.0,
//            characteristics = mapOf("Feature" to "Value")
//        )
//
//        addOrUpdateSaleInFirebase(newSale)
//
//        Thread.sleep(3000)
//
//        deleteSaleFromFirebase(saleId)
//
//        Thread.sleep(3000)
//
//        firebaseDatabase.getReference("sales/$saleId").get().addOnSuccessListener { dataSnapshot ->
//            assertTrue(dataSnapshot.value == null)
//        }
//    }
//}
//
