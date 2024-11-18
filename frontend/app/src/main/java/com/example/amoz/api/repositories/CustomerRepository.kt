package com.example.amoz.api.repositories

import com.example.amoz.api.models.Customer
import com.example.amoz.api.services.CustomerService
import com.example.amoz.api.models.CustomerB2B
import com.example.amoz.api.models.CustomerB2C
import com.example.amoz.api.requests.CustomerB2BCreateRequest
import com.example.amoz.api.requests.CustomerB2CCreateRequest
import com.example.amoz.api.sealed.CustomerResult
import kotlinx.serialization.json.JsonElement
import java.util.UUID
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val customerService: CustomerService
) : BaseRepository() {

    suspend fun getCustomerDetails(customerId: UUID): CustomerResult? {
        val customerJson = performRequest {
            customerService.getCustomerDetails(customerId)
        }
        if (customerJson != null) {
            return CustomerResult.createCustomerDetails(customerJson)
        }
        return null
    }

    suspend fun createCustomerB2B(request: CustomerB2BCreateRequest): CustomerB2B? {
        return performRequest {
            customerService.createCustomerB2B(request)
        }
    }

    suspend fun createCustomerB2C(request: CustomerB2CCreateRequest): CustomerB2C? {
        return performRequest {
            customerService.createCustomerB2C(request)
        }
    }

    suspend fun updateCustomerB2B(customerId: UUID, request: CustomerB2BCreateRequest): CustomerB2B? {
        return performRequest {
            customerService.updateCustomerB2B(customerId, request)
        }
    }

    suspend fun updateCustomerB2C(customerId: UUID, request: CustomerB2CCreateRequest): CustomerB2C? {
        return performRequest {
            customerService.updateCustomerB2C(customerId, request)
        }
    }

    suspend fun getAllCustomersB2B(): List<CustomerB2B> {
        return performRequest {
            customerService.getAllCustomersB2B()
        } ?: listOf()
    }

    suspend fun getAllCustomersB2C(): List<CustomerB2C> {
        return performRequest {
            customerService.getAllCustomersB2C()
        } ?: listOf()
    }
}
