package com.example.amoz.api.requests

import kotlinx.serialization.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Serializable
data class ContactPersonCreateRequest(

    @field:NotBlank(message = "Contact number is required.")
    val contactNumber: String,

    @field:Email(message = "Email must be valid.")
    val emailAddress: String? = null
)
