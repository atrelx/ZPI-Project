package com.example.amoz.requests

import kotlinx.serialization.Serializable
import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class CategoryCreateRequest(

    @field:NotBlank(message = "Category name is required")
    val name: String,

    @Contextual val parentCategoryId: UUID? = null
)
