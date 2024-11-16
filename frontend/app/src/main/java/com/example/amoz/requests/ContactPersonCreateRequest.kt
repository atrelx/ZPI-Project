package com.example.amoz.requests

import kotlinx.serialization.Serializable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Serializable
data class ContactPersonCreateRequest(

    @field:NotBlank(message = "Contact number is required.")
    val contactNumber: String,

    @field:Email(message = "Email must be valid.")
    val emailAddress: String? = null
)
