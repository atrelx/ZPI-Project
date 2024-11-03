package com.example.bussiness.data

import java.time.LocalDate

data class Person(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String? = null,
    val personPhoto: Int? = null,
    val registeredSince: LocalDate = LocalDate.now(),
)

data class B2BCustomer(
    val companyName: String = "",
    val email: String = "",
    val companyAddress: String = "",
    val companyIdentifier: String = "",
    val phoneNumber: String? = null,
    val registeredSince: LocalDate = LocalDate.now(),
)