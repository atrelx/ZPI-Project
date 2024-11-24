package com.example.amoz.api.requests

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductVariantDetails
import com.example.validation.annotations.DecimalMin
import com.example.validation.annotations.NotNullable
import com.example.validation.annotations.Positive
import com.example.validation.annotations.Size
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.*

@Serializable
data class ProductVariantCreateRequest(

    @Serializable(with = UUIDSerializer::class)
    @field:NotNullable(nameOfField = "Product ID")
    val productID: UUID,

    @field:Positive(nameOfField = "Product variant code")
    @field:NotNullable(nameOfField = "Product variant code")
    val productVariantCode: Int,

    @field:NotNullable(nameOfField = "Product ID")
    val stock: StockCreateRequest,

    val weight: WeightCreateRequest? = null,

    val dimensions: DimensionsCreateRequest? = null,

    val variantAttributes: List<AttributeCreateRequest> = listOf(),

    @field:Size(max = 100, nameOfField = "Variant name")
    val variantName: String? = null,

    @field:DecimalMin(value = "0.0", inclusive = false, nameOfField = "Variant price")
    @field:NotNullable(nameOfField = "Variant price")
    @Serializable(with = BigDecimalSerializer::class)
    val variantPrice: BigDecimal
) : ValidatableRequest<ProductVariantCreateRequest>() {
    constructor(productVariant: ProductVariantDetails, productID: UUID) : this(
        productID = productID,
        productVariantCode = productVariant.code,
        stock = StockCreateRequest(productVariant.stock),
        weight = if (productVariant.weight != null) {
            WeightCreateRequest(productVariant.weight)
        } else {
            null
        },
        dimensions = if (productVariant.dimensions != null) {
            DimensionsCreateRequest(productVariant.dimensions)
        } else {
            null
        },
        variantAttributes = productVariant.variantAttributes.map { AttributeCreateRequest(it) },
        variantName = productVariant.variantName,
        variantPrice = productVariant.variantPrice
    )
}
