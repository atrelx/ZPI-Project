package com.example.amoz.requests

import kotlinx.serialization.Serializable
import jakarta.validation.constraints.NotNull

@Serializable
data class CustomerB2CCreateRequest(

    val customer: CustomerCreateRequest,

    val person: PersonCreateRequest
)
