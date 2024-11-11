package com.example.amoz.data

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

// --------------------- Product ---------------------
data class ProductVariant(
    val id: String = "",
    var name: String = "",
    var description: String = "",
    var price: String = "",
    var imageUrl: String = "",
    var attributes: Map<String, String> = emptyMap()
)

data class ProductTemplate(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val productVendor: String = "",
    val attributes: Map<String, String> = emptyMap(),
    val mainVariantId: Int? = null
)

// --------------------- Address ---------------------
data class Address (
    val street: String = "",
    val houseNumber: String = "",
    val apartmentNumber: String = "",
    val city: String = "",
    val postalCode: String = "",
    val additionalInfo: String = "",
)

// --------------------- Navigation Items ---------------------
data class NavItem(
    val title: Int,
    val screenRoute: String,
    val icon: ImageVector,
    val unselectedIcon: ImageVector? = null,
)

