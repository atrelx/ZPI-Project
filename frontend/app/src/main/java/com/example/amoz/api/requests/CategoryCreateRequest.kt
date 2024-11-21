package com.example.amoz.api.requests

import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID
import javax.validation.constraints.NotBlank

@Serializable
data class CategoryCreateRequest(

    @field:NotBlank(message = "Category name is required")
    val name: String,

    @Serializable(with = UUIDSerializer::class)
    val parentCategoryId: UUID? = null
) : ValidatableRequest<CategoryCreateRequest>()
