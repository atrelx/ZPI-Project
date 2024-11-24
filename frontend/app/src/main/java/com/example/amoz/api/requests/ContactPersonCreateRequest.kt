package com.example.amoz.api.requests

import com.example.validation.annotations.Email
import com.example.validation.annotations.NotBlank
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.ContactPerson
import com.example.amoz.models.Customer
import kotlinx.serialization.Serializable
@Serializable
data class ContactPersonCreateRequest(

    @field:NotBlank(nameOfField = "Contact number")
    val contactNumber: String = "",

    @field:Email(nameOfField = "Email address")
    val emailAddress: String? = null
) : ValidatableRequest<ContactPersonCreateRequest>() {
    constructor(contactPerson: ContactPerson) : this(
        contactNumber = contactPerson.contactNumber,
        emailAddress = contactPerson.emailAddress
    )
}
