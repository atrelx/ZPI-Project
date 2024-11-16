package com.example.amoz.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class ContactPerson(
    @Contextual val contactPersonId: UUID,
    val contactNumber: String,
    val emailAddress: String
)
