package com.example.amoz.api.requests

import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Customer
import kotlinx.serialization.Serializable
import javax.validation.constraints.NotNull

@Serializable
data class CustomerCreateRequest(

    var contactPerson: ContactPersonCreateRequest = ContactPersonCreateRequest(),

    val defaultDeliveryAddress: AddressCreateRequest? = null
) : ValidatableRequest<CustomerCreateRequest>() {
    constructor(customer: Customer): this(
        contactPerson = ContactPersonCreateRequest(customer.contactPerson),
        defaultDeliveryAddress = customer.defaultDeliveryAddress?.let { AddressCreateRequest(it) }
    )
}
