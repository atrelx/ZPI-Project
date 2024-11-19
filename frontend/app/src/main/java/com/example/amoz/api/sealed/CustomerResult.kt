package com.example.amoz.api.sealed

import com.example.amoz.api.extensions.tryParse
import com.example.amoz.api.models.CustomerB2B
import com.example.amoz.api.models.CustomerB2C
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

sealed class CustomerResult {
    data class B2B(val data: CustomerB2B) : CustomerResult()
    data class B2C(val data: CustomerB2C) : CustomerResult()

    companion object {
        fun createCustomerDetails(customerJson: JsonElement): CustomerResult {
            val customerB2B = customerJson.tryParse<CustomerB2B>()
            if (customerB2B != null) {
                return B2B(customerB2B)
            }

            val customerB2C = customerJson.tryParse<CustomerB2C>()
            if (customerB2C != null) {
                return B2C(customerB2C)
            }
            throw Exception("Invalid customer type")
        }
    }
}


