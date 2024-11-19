package com.example.amoz.models

import com.example.amoz.api.enums.Status
import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.LocalDateSerializer
import com.example.amoz.api.serializers.LocalDateTimeSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class ProductOrderDetails(
    @Serializable(with = UUIDSerializer::class)
    val productOrderId: UUID,
    val status: Status,
    val customer: com.example.amoz.models.Customer? = null,
    val address: com.example.amoz.models.Address? = null,
    @Serializable(with = UUIDSerializer::class)
    val invoiceId: UUID? = null,
    val productOrderItems: List<com.example.amoz.models.ProductOrderItemDetails> = listOf(),
    @Serializable(with = BigDecimalSerializer::class)
    val totalDue: BigDecimal,
    val trackingNumber: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timeOfSending: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timeOfCreation: LocalDateTime
)
