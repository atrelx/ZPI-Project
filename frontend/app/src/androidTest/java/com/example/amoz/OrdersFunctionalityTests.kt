package com.example.amoz

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.amoz.api.repositories.CategoryRepository
import com.example.amoz.api.repositories.CustomerRepository
import com.example.amoz.api.repositories.ProductOrderRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.view_models.CategoriesViewModel
import com.example.amoz.view_models.OrdersViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class OrdersFunctionalityTests {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var ordersRepository: ProductOrderRepository

    @Inject
    lateinit var customerRepository: CustomerRepository

    @Inject
    lateinit var productsRepository: ProductVariantRepository

    private lateinit var ordersViewModel: OrdersViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        ordersViewModel = OrdersViewModel(
            context = ApplicationProvider.getApplicationContext(),
            ordersRepository,
            productsRepository,
            customerRepository)
    }

    @After
    fun clearTestData() {

    }
}