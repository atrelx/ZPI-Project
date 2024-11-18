package com.example.amoz.api.requests

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotNull

@Serializable
data class CustomerB2CCreateRequest(

    val customer: CustomerCreateRequest,

    val person: PersonCreateRequest
)
