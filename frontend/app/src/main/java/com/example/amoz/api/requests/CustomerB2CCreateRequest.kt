package com.example.amoz.api.requests

import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.CustomerB2C
import kotlinx.serialization.Serializable
import javax.validation.constraints.NotNull

@Serializable
data class CustomerB2CCreateRequest(

    val customer: CustomerCreateRequest = CustomerCreateRequest(),

    var person: PersonCreateRequest = PersonCreateRequest()
) : ValidatableRequest<CustomerB2CCreateRequest>() {
    constructor(customerB2C: CustomerB2C): this(
        customer = CustomerCreateRequest(customerB2C.customer),
        person = PersonCreateRequest(customerB2C.person)
    )
}
