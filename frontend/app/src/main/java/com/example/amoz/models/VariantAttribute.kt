package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class VariantAttribute(
    @Serializable(with = UUIDSerializer::class)
    val variantAttributeId: UUID,
    val attribute: com.example.amoz.models.Attribute,
    val value: String?
)