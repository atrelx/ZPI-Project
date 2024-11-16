package com.example.amoz.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class VariantAttribute(
    @Contextual val variantAttributeId: UUID,
    val attribute: Attribute,
    val value: String?
)