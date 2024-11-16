package com.example.amoz.requests

import kotlinx.serialization.Serializable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Serializable
data class CompanyCreateRequest(

    @field:NotBlank(message = "Company number is required")
    @field:Size(max = 50, message = "Company number should not exceed 50 characters")
    val companyNumber: String,

    @field:NotBlank(message = "Country of registration is required")
    @field:Size(max = 100, message = "Country of registration should not exceed 100 characters")
    val countryOfRegistration: String,

    @field:NotBlank(message = "Company name is required")
    @field:Size(max = 100, message = "Company name should not exceed 100 characters")
    val name: String,

    val address: AddressCreateRequest,

    val regon: String? = null
)
