package com.example.amoz.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable

@Serializable
data class AddressCreateRequest(
    @field:NotBlank
    @field:Size(max = 50)
    val city: String,

    @field:NotBlank
    @field:Size(max = 50)
    val street: String,

    @field:NotBlank
    @field:Size(max = 10)
    val streetNumber: String,

    @field:NotBlank
    @field:Size(max = 10)
    val apartmentNumber: String,

    @field:NotBlank
    @field:Size(max = 10)
    val postalCode: String,

    @field:Size(max = 255)
    val additionalInformation: String? = null
)
