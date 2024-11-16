package com.example.amoz.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductOrderItemSummary(
    @Contextual val productOrderItemId: UUID,
    val productVariant: ProductVariantSummary? = null,
    @Contextual val unitPrice: BigDecimal,
    val amount: Int,
    val productName: String? = null
)
