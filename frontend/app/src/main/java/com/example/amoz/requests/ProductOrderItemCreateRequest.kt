package com.example.amoz.requests

import kotlinx.serialization.Serializable
import jakarta.validation.constraints.*
import kotlinx.serialization.Contextual
import java.util.*

@Serializable
data class ProductOrderItemCreateRequest(

    @Contextual val productVariantId: UUID,

    @field:Min(value = 1, message = "Amount must be at least 1")
    val amount: Int
)
