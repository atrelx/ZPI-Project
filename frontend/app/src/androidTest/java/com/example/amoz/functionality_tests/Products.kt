package com.example.amoz.functionality_tests

import android.content.Context
import android.util.Log
import androidx.test.filters.LargeTest
import com.example.amoz.api.repositories.ProductRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.api.requests.AttributeCreateRequest
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.app.AppPreferences
import com.example.amoz.view_models.ProductsViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

@HiltAndroidTest
@LargeTest
class Products {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @ApplicationContext
    lateinit var context: Context
    @Inject
    lateinit var productRepository: ProductRepository
    @Inject
    lateinit var productVariantRepository: ProductVariantRepository
    @Inject
    lateinit var appPreferences: AppPreferences
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var testProductsIdList: MutableList<UUID>
    private lateinit var testProductCreateRequest: ProductCreateRequest



    @Before
    fun setup() {
        hiltRule.inject()
        testProductsIdList = mutableListOf()
        testProductCreateRequest = ProductCreateRequest(
            name = "Test Product",
            price = BigDecimal("95.99"),
            categoryId = null,
            description = "Simple test product",
            productAttributes = listOf(
                AttributeCreateRequest(
                    attributeName = "Color",
                    value = "Blue"
                )
            )
        )
        productsViewModel = ProductsViewModel(context, productRepository, productVariantRepository, appPreferences)
    }



    @Test
    fun testProductListFetch() {
        productsViewModel.fetchProductsList {
            assertNotNull(it)
        }
    }

    @Test
    fun createValidProduct() {
        val productToCreate = this.testProductCreateRequest

        productsViewModel.createProduct(productToCreate) { productDetails ->
            val productId = productDetails.productId
            addProductIdToTestData(productId)
            assertProductInList(productId) { assert(it) }
        }
    }


    @Test
    fun createInvalidProduct() {
        val invalidProductsList = listOf(
            testProductCreateRequest.copy(name = null),
            testProductCreateRequest.copy(name = "test".repeat(50)),
            testProductCreateRequest.copy(price = null),
            testProductCreateRequest.copy(price = BigDecimal("0.123")),
            testProductCreateRequest.copy(price = BigDecimal("-123")),
            testProductCreateRequest.copy(description = "test".repeat(1000)),
        )

        invalidProductsList.forEach { productToCreate ->
            Log.d("Current product create request", productToCreate.toString())
            assertThrows(IllegalArgumentException::class.java) {
                productsViewModel.createProduct(productToCreate) {
                    addProductIdToTestData(it.productId)
                }
            }
        }
    }

    @After
    fun clearTestData() {
        testProductsIdList.forEach { productsViewModel.deleteProduct(it) }
    }

    private fun assertProductInList(productId: UUID, isFound: (Boolean) -> Unit) {
        productsViewModel.fetchProductsList { productsList ->
            val foundProduct = productsList.any { it.productId == productId }
            isFound(foundProduct)
        }
    }

    private fun addProductIdToTestData(productId: UUID) {
        testProductsIdList.add(productId)
    }

}