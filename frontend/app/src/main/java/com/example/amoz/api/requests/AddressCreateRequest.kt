package com.example.amoz.api.requests


import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Address
import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Serializable
data class AddressCreateRequest(
    @field:NotBlank
    @field:Size(max = 50)
    var city: String = "",

    @field:NotBlank
    @field:Size(max = 50)
    var street: String = "",

    @field:NotBlank
    @field:Size(max = 10)
    var streetNumber: String = "",

    @field:Size(max = 10)
    var apartmentNumber: String? = null,

    @field:NotBlank
    @field:Size(max = 10)
    var postalCode: String = "",

    @field:Size(max = 255)
    var additionalInformation: String? = null
) : ValidatableRequest<AddressCreateRequest>() {
    constructor(address: Address) : this(
        city = address.city,
        street = address.street,
        streetNumber = address.streetNumber,
        apartmentNumber = address.apartmentNumber,
        postalCode = address.postalCode,
        additionalInformation = address.additionalInformation
    )
}
