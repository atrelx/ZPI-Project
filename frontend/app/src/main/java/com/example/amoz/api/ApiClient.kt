package com.example.amoz.api

import android.content.Context
import com.example.amoz.helpers.TokenManager
import com.example.amoz.services.AttributeService
import com.example.amoz.services.AuthenticationService
import com.example.amoz.services.CategoryService
import com.example.amoz.services.CompanyService
import com.example.amoz.services.CustomerService
import com.example.amoz.services.EmployeeService
import com.example.amoz.services.ProductOrderService
import com.example.amoz.services.ProductService
import com.example.amoz.services.ProductVariantService
import com.example.amoz.services.UserService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApiClient {
    private const val BASE_URL = "https://amoz-backend.azurewebsites.net/"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private fun createRetrofitInstance(context: Context): Retrofit {
        val tokenManager = TokenManager(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    fun getAttributeService(context: Context): AttributeService {
        return createRetrofitInstance(context).create(AttributeService::class.java)
    }

    fun getAuthenticationService(context: Context): AuthenticationService {
        return createRetrofitInstance(context).create(AuthenticationService::class.java)
    }

    fun getCategoryService(context: Context): CategoryService {
        return createRetrofitInstance(context).create(CategoryService::class.java)
    }

    fun getCompanyService(context: Context): CompanyService {
        return createRetrofitInstance(context).create(CompanyService::class.java)
    }

    fun getCustomerService(context: Context): CustomerService {
        return createRetrofitInstance(context).create(CustomerService::class.java)
    }

    fun getEmployeeService(context: Context): EmployeeService {
        return createRetrofitInstance(context).create(EmployeeService::class.java)
    }

    fun getProductOrderService(context: Context): ProductOrderService {
        return createRetrofitInstance(context).create(ProductOrderService::class.java)
    }

    fun getProductService(context: Context): ProductService {
        return createRetrofitInstance(context).create(ProductService::class.java)
    }

    fun getProductVariantService(context: Context): ProductVariantService {
        return createRetrofitInstance(context).create(ProductVariantService::class.java)
    }

    fun getUserService(context: Context): UserService {
        return createRetrofitInstance(context).create(UserService::class.java)
    }
}