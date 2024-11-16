package com.example.amoz.requests

import kotlinx.serialization.Serializable
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Serializable
data class CustomerB2BCreateRequest(

    val customer: CustomerCreateRequest,

    @field:NotEmpty(message = "Company number must not be empty")
    @field:Size(max = 30, message = "Company number cannot exceed 30 characters")
    val companyNumber: String,

    @field:NotEmpty(message = "Name on invoice must not be empty")
    @field:Size(max = 255, message = "Name on invoice cannot exceed 255 characters")
    val nameOnInvoice: String,

    val address: AddressCreateRequest
)
