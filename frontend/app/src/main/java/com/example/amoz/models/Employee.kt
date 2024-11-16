package com.example.amoz.models

import com.example.amoz.enums.RoleInCompany
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Employee(
    @Contextual val employeeId: UUID,
    val user: User,
    val contactPerson: ContactPerson,
    val person: Person,
    val roleInCompany: RoleInCompany,
    @Contextual val employmentDate: LocalDate
)
