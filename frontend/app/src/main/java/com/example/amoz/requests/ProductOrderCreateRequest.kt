package com.example.amoz.requests

import com.example.amoz.enums.Status
import kotlinx.serialization.Serializable
import jakarta.validation.constraints.*
import kotlinx.serialization.Contextual
import java.time.LocalDateTime
import java.util.*

@Serializable
data class ProductOrderCreateRequest(

    val status: Status,

    @field:Size(min = 1, message = "At least one product order item is required")
    val productOrderItems: List<ProductOrderItemCreateRequest>,

    val address: AddressCreateRequest? = null,

    @Contextual val customerId: UUID? = null,

    @field:Size(max = 10, message = "Tracking number cannot exceed 10 characters")
    val trackingNumber: String? = null,

    @field:PastOrPresent(message = "Time of sending cannot be in the future")
    @Contextual val timeOfSending: LocalDateTime? = null
)
