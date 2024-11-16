package com.example.amoz.models

import com.example.amoz.enums.Sex
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Person(
    @Contextual val personId: UUID,
    val name: String,
    val surname: String,
    @Contextual val dateOfBirth: LocalDate,
    val sex: Sex
)
