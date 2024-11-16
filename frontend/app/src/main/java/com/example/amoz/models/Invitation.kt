package com.example.amoz.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Invitation(
    @Contextual val invitationId: UUID,
    @Contextual val companyId: UUID,
    @Contextual val employeeId: UUID,
    @Contextual val token: UUID
)
