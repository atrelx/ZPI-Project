package com.example.amoz.requests

import kotlinx.serialization.Serializable
import jakarta.validation.constraints.NotNull

@Serializable
data class CustomerCreateRequest(

    val contactPerson: ContactPersonCreateRequest,

    val defaultDeliveryAddress: AddressCreateRequest? = null
)
