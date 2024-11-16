package com.example.amoz.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Address(
    @Contextual val addressId: UUID,
    val city: String,
    val street: String,
    val streetNumber: String,
    val apartmentNumber: String? = null,
    val postalCode: String,
    val additionalInformation: String? = null
)

