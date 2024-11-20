package com.example.amoz.api.requests

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.*
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Positive

@Serializable
data class ProductVariantCreateRequest(

    @Serializable(with = UUIDSerializer::class)
    val productID: UUID,

    @field:Positive(message = "Product variant code must be a positive number")
    val productVariantCode: Int,

    val stock: StockCreateRequest,

    val weight: WeightCreateRequest? = null,

    val dimensions: DimensionsCreateRequest? = null,

    val variantAttributes: List<AttributeCreateRequest>,

    val variantName: String? = null,

    @field:DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Serializable(with = BigDecimalSerializer::class)
    val variantPrice: BigDecimal? = null
) : ValidatableRequest<ProductVariantCreateRequest>()
