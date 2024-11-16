package com.example.amoz.models

import com.example.amoz.enums.Status
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class ProductOrderDetails(
    @Contextual val productOrderId: UUID,
    @Contextual val status: Status,
    val customer: Customer? = null,
    val address: Address? = null,
    @Contextual val invoiceId: UUID? = null,
    val productOrderItems: List<ProductOrderItemDetails> = listOf(),
    @Contextual val totalDue: BigDecimal,
    val trackingNumber: String? = null,
    @Contextual val timeOfSending: LocalDateTime? = null,
    @Contextual val timeOfCreation: LocalDateTime
)
