package com.example.amoz.models

import kotlinx.serialization.Serializable

@Serializable
data class CustomerB2C(
    val customer: com.example.amoz.models.Customer,
    val person: com.example.amoz.models.Person
)
