package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class CategoryDetails(
    @Serializable(with = UUIDSerializer::class)
    val categoryId: UUID,
    val name: String,
    val categoryLevel: Short,
    val parentCategory: CategoryDetails? = null
)
