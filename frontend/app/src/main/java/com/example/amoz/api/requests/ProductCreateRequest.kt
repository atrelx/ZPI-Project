package com.example.amoz.api.requests

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.ProductDetails
import kotlinx.serialization.Serializable
import javax.validation.constraints.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Required
import java.math.BigDecimal
import java.util.*

@Serializable
data class ProductCreateRequest(

    @field:NotBlank(message = "Name is required")
    @field:Size(max = 100, message = "Name cannot exceed 100 characters")
    val name: String? = null,

    @field:DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @field:Digits(integer = 10, fraction = 2, message = "Price must be a valid amount with up to 10 digits and 2 decimal places")
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal? = null,

    @Required
    @Serializable(with = UUIDSerializer::class)
    val categoryId: UUID? = null,

    val description: String? = null,

    val brand: String? = null,

    val productVariantIds: List<@Serializable(with = UUIDSerializer::class) UUID> = listOf(),

    val productAttributes: List<AttributeCreateRequest> = listOf()
) : ValidatableRequest<ProductCreateRequest>() {
    constructor(productDetails: ProductDetails?) :this (
        name = productDetails?.name,
        price = productDetails?.price,
        categoryId = productDetails?.category?.categoryId,
        description = productDetails?.description,
        brand = productDetails?.brand,
        productVariantIds = emptyList(),
        productAttributes = productDetails?.productAttributes?.map {
            AttributeCreateRequest(
                attributeName = it.attribute.attributeName,
                value = it.value
            )
        } ?: emptyList()
    )
}
