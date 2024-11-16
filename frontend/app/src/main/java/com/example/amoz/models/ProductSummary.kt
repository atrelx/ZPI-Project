package com.example.amoz.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductSummary(
    @Contextual val productId: UUID,
    val name: String,
    @Contextual val price: BigDecimal,
    val category: CategorySummary,
    val mainProductVariant: ProductVariantDetails? = null,
    val description: String? = null,
    val brand: String? = null
)
