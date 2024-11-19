package com.example.amoz.models

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductOrderItemDetails(
    @Serializable(with = UUIDSerializer::class)
    val productOrderItemId: UUID,
    val productVariant: com.example.amoz.models.ProductVariantDetails? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val unitPrice: BigDecimal,
    val amount: Int,
    val productName: String? = null
)
