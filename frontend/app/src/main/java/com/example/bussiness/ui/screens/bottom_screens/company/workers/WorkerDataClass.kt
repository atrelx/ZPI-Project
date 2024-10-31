package com.example.bussiness.ui.screens.bottom_screens.company.workers

import com.example.bussiness.R
import java.time.LocalDate

data class Person(
    val firstName: String = "",
    val lastName: String = "",
    val personPhoto: Int? = null,
    val email: String = "",
    val phoneNumber: String? = null,
    val workSince: LocalDate = LocalDate.now(),
)

val testWorkers = listOf(
    Person(
        firstName = "Andrii",
        lastName = "Drobitko",
        personPhoto = R.drawable.andrii_photo,
        email = "anddro458@gmail.com",
        phoneNumber = "+48576475004",
    ),
    Person(
        firstName = "Łukasz",
        lastName = "Bielawski",
        personPhoto = R.drawable.lukasz_photo,
        email = "lukasz.bielawski@gmail.com",
    ),
    Person(
        firstName = "Mikołaj",
        lastName = "Kwinta",
        personPhoto = R.drawable.mikolaj_photo,
        email = "mikołaj.kwinta@gmail.com",
        phoneNumber = "+48123456789",
    ),
    Person(
        firstName = "Oleksii",
        lastName = "Adamenko",
        personPhoto = R.drawable.oleksii_photo,
        email = "oleksii.adamenko@gmail.com",
    ),
)