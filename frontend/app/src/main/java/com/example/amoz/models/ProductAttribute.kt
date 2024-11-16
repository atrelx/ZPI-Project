package com.example.amoz.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class ProductAttribute(
    @Contextual val productAttributeId: UUID,
    val attribute: Attribute,
    val value: String? = null
)
