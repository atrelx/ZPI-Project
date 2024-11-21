package com.example.amoz.api.requests

import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import kotlinx.serialization.Serializable
import javax.validation.constraints.*
import kotlinx.serialization.Contextual
import java.util.*

@Serializable
data class ProductOrderItemCreateRequest(

    @Serializable(with = UUIDSerializer::class)
    val productVariantId: UUID,

    @field:Min(value = 1, message = "Amount must be at least 1")
    val amount: Int
) : ValidatableRequest<ProductOrderItemCreateRequest>()
