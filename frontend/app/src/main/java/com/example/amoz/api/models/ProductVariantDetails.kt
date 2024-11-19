package com.example.amoz.api.models

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductVariantDetails(
    @Serializable(with = UUIDSerializer::class)
    val productVariantId: UUID,
    val code: Int,
    val stock: Stock? = null,
    val dimensions: Dimensions? = null,
    val weight: Weight? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val variantPrice: BigDecimal,
    val variantName: String,
    val variantAttributes: List<VariantAttribute> = listOf()
)
