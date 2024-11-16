package com.example.amoz.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductVariantSummary(
    @Contextual val productVariantId: UUID,
    val code: Int,
    @Contextual val variantPrice: BigDecimal,
    val variantName: String
)
