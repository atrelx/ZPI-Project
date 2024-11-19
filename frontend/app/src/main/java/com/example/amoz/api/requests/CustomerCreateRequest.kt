package com.example.amoz.api.requests

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotNull

@Serializable
data class CustomerCreateRequest(

    val contactPerson: ContactPersonCreateRequest,

    val defaultDeliveryAddress: AddressCreateRequest? = null
)
