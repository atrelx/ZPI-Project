package com.example.amoz.requests

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Positive
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.*

@Serializable
data class ProductVariantCreateRequest(

    @Contextual val productID: UUID,

    @field:Positive(message = "Product variant code must be a positive number")
    val productVariantCode: Int,

    val stock: StockCreateRequest,

    val weight: WeightCreateRequest? = null,

    val dimensions: DimensionsCreateRequest? = null,

    val variantAttributes: List<AttributeCreateRequest>,

    val variantName: String? = null,

    @field:DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Contextual val variantPrice: BigDecimal? = null
)
