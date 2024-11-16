package com.example.amoz.models

import com.example.amoz.enums.Status
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class ProductOrderSummary(
    @Contextual val productOrderId: UUID,
    val status: Status,
    val sampleProductOrderItem: ProductOrderItemSummary,
    @Contextual val totalDue: BigDecimal,
    val trackingNumber: String? = null,
    @Contextual val timeOfSending: LocalDateTime? = null,
    @Contextual val timeOfCreation: LocalDateTime
)
