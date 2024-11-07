package com.example.bussiness.ui.screens.bottom_screens.company.employees

import com.example.bussiness.R
import com.example.bussiness.data.Employee
import com.example.bussiness.data.Person

val testEmployees = mutableListOf(
    Employee(
        employeeId = 0,
        firstName = "Andrii",
        lastName = "Drobitko",
        personPhoto = R.drawable.andrii_photo,
        sex = "Male",
        email = "anddro458@gmail.com",
        phoneNumber = "+48576475004",
    ),
    Employee(
        employeeId = 1,
        firstName = "Łukasz",
        lastName = "Bielawski",
        sex = "Male",
        personPhoto = R.drawable.lukasz_photo,
        email = "lukasz.bielawski@gmail.com",
    ),
    Employee(
        employeeId = 2,
        firstName = "Mikołaj",
        lastName = "Kwinta",
        sex = "Male",
        personPhoto = R.drawable.mikolaj_photo,
        email = "mikołaj.kwinta@gmail.com",
        phoneNumber = "+48123456789",
    ),
    Employee(
        employeeId = 3,
        firstName = "Oleksii",
        lastName = "Adamenko",
        sex = "Male",
        personPhoto = R.drawable.oleksii_photo,
        email = "oleksii.adamenko@gmail.com",
    ),
)