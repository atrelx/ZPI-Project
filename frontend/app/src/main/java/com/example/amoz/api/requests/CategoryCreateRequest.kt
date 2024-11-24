package com.example.amoz.api.requests

import com.example.validation.annotations.NotBlank
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.CategoryDetails
import com.example.amoz.models.CategorySummary
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class CategoryCreateRequest(

    @field:NotBlank(nameOfField = "Category name is required")
    val name: String,

    @Serializable(with = UUIDSerializer::class)
    val parentCategoryId: UUID? = null
) : ValidatableRequest<CategoryCreateRequest>() {
    constructor(category: CategoryDetails) : this(
        name = category.name,
        parentCategoryId = category.parentCategory?.categoryId
    )
}
