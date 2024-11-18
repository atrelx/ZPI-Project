package com.example.amoz.api.requests

import com.example.amoz.api.enums.Status
import com.example.amoz.api.serializers.LocalDateTimeSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import javax.validation.constraints.*
import kotlinx.serialization.Contextual
import java.time.LocalDateTime
import java.util.*

@Serializable
data class ProductOrderCreateRequest(

    val status: com.example.amoz.api.enums.Status,

    @field:Size(min = 1, message = "At least one product order item is required")
    val productOrderItems: List<ProductOrderItemCreateRequest>,

    val address: AddressCreateRequest? = null,

    @Serializable(with = UUIDSerializer::class) val customerId: UUID? = null,

    @field:Size(max = 10, message = "Tracking number cannot exceed 10 characters")
    val trackingNumber: String? = null,

    @field:PastOrPresent(message = "Time of sending cannot be in the future")
    @Serializable(with = LocalDateTimeSerializer::class)
    val timeOfSending: LocalDateTime? = null
)
