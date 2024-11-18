package com.example.amoz.api.requests

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

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
