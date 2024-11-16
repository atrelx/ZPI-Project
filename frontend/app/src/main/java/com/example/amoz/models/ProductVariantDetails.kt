package com.example.amoz.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductVariantDetails(
    @Contextual val productVariantId: UUID,
    val code: Int,
    val stock: Stock? = null,
    val dimensions: Dimensions? = null,
    val weight: Weight? = null,
    @Contextual val variantPrice: BigDecimal,
    val variantName: String,
    val variantAttributes: List<VariantAttribute> = listOf()
)
