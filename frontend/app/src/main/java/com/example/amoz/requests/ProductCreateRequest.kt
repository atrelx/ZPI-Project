package com.example.amoz.requests

import kotlinx.serialization.Serializable
import jakarta.validation.constraints.*
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.util.*

@Serializable
data class ProductCreateRequest(

    @field:NotBlank(message = "Name is required")
    @field:Size(max = 100, message = "Name cannot exceed 100 characters")
    val name: String,

    @field:DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @field:Digits(integer = 10, fraction = 2, message = "Price must be a valid amount with up to 10 digits and 2 decimal places")
    @Contextual val price: BigDecimal,

    @Contextual val categoryId: UUID,

    val description: String? = null,

    val brand: String? = null,

    val productVariantIds: List<@Contextual UUID> = listOf(),

    val productAttributes: List<AttributeCreateRequest> = listOf()
)
