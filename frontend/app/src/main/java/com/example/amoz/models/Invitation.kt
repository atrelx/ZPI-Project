package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Invitation(
    @Serializable(with = UUIDSerializer::class)
    val invitationId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val companyId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val employeeId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val token: UUID
)
