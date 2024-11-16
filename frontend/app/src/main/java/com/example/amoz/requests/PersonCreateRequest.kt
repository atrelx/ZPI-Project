package com.example.amoz.requests

import com.example.amoz.enums.Sex
import kotlinx.serialization.Serializable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import kotlinx.serialization.Contextual
import java.time.LocalDate

@Serializable
data class PersonCreateRequest(

    @field:NotBlank(message = "Name is required.")
    @field:Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters.")
    val name: String,

    @field:NotBlank(message = "Surname is required.")
    @field:Size(min = 2, max = 30, message = "Surname must be between 2 and 30 characters.")
    val surname: String,

    @field:Past(message = "Date of birth must be a date in the past.")
    @Contextual val dateOfBirth: LocalDate,

    val sex: Sex
)
