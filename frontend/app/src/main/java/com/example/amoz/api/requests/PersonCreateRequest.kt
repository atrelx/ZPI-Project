package com.example.amoz.api.requests

import com.example.amoz.enums.Sex
import com.example.amoz.api.serializers.LocalDateSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Person
import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Past
import javax.validation.constraints.Size
import kotlinx.serialization.Contextual
import java.time.LocalDate

@Serializable
data class PersonCreateRequest(

    @field:NotBlank(message = "Name is required.")
    @field:Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters.")
    val name: String = "",

    @field:NotBlank(message = "Surname is required.")
    @field:Size(min = 2, max = 30, message = "Surname must be between 2 and 30 characters.")
    val surname: String = "",

    @field:Past(message = "Date of birth must be a date in the past.")
    @Serializable(with = LocalDateSerializer::class)
    val dateOfBirth: LocalDate = LocalDate.of(2000, 1, 1),

    val sex: Sex = Sex.M
) : ValidatableRequest<PersonCreateRequest>() {
    constructor(person: Person): this(
        name = person.name,
        surname = person.surname,
        dateOfBirth = person.dateOfBirth,
        sex = person.sex
    )
}
