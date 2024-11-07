package com.example.bussiness.data

import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

data class Person(
    val personId: Int? = null,
    val firstName: String = "",
    val lastName: String = "",
    val sex: String = "",
    val email: String = "",
    val phoneNumber: String? = null,
    val personPhoto: Int? = null,
    val dateOfBirth: LocalDate = LocalDate.now(),
)

data class Employee(
    val employeeId: Int? = null,
    val firstName: String = "",
    val lastName: String = "",
    val sex: String = "",
    val email: String = "",
    val phoneNumber: String? = null,
    val personPhoto: Int? = null,
    val dateOfBirth: LocalDate = LocalDate.now(),
    val employmentDate: LocalDate = LocalDate.now(),
)

data class B2BCustomer(
    val companyName: String = "",
    val email: String = "",
    val companyAddress: String = "",
    val companyIdentifier: String = "",
    val phoneNumber: String? = null,
)

data class NavItem(
    val title: Int,
    val screenRoute: String? = null,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)