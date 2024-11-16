package com.example.amoz.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductDetails(
    @Contextual val productId: UUID,
    val name: String,
    @Contextual  val price: BigDecimal,
    val category: CategoryDetails,
    val mainProductVariant: ProductVariantDetails? = null,
    val productAttributes: List<ProductAttribute>,
    val description: String? = null,
    val brand: String? = null
)
