package com.example.amoz.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class CategorySummary(
    @Contextual val categoryId: UUID,
    val name: String,
    val categoryLevel: Short
)
