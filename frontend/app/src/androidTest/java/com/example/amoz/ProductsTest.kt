package com.example.amoz

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.amoz.api.repositories.ProductRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.app.AppPreferences
import com.example.amoz.view_models.ProductsViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.UUID
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class ProductsTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var productRepository: ProductRepository
    @Inject
    lateinit var productVariantRepository: ProductVariantRepository
    @Inject
    lateinit var appPreferences: AppPreferences
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var testProductsIdList: MutableList<UUID>

    @Before
    fun setup() {
        hiltRule.inject()
        testProductsIdList = mutableListOf()
    //    productsViewModel = ProductsViewModel(productRepository, productVariantRepository, appPreferences)
    }

    @Test
    fun testProductListFetch() {
        productsViewModel.fetchProductsList {
            assertNotNull(it)
        }
    }

    @Test
    fun createProduct() {

    }

}