package com.example.amoz.api.requests

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

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
