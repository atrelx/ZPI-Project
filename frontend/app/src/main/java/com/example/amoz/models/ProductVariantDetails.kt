package com.example.amoz.models

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
    val stock: com.example.amoz.models.Stock? = null,
    val dimensions: com.example.amoz.models.Dimensions? = null,
    val weight: com.example.amoz.models.Weight? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val variantPrice: BigDecimal,
    val variantName: String,
    val variantAttributes: List<com.example.amoz.models.VariantAttribute> = listOf()
)
