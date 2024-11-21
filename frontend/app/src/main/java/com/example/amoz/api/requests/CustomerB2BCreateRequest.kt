package com.example.amoz.api.requests

import android.provider.Telephony.Mms.Addr
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.CustomerB2B
import kotlinx.serialization.Serializable
import org.hibernate.validator.internal.metadata.facets.Validatable
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Serializable
data class CustomerB2BCreateRequest(

    val customer: CustomerCreateRequest = CustomerCreateRequest(),

    @field:NotEmpty(message = "Company number must not be empty")
    @field:Size(max = 30, message = "Company number cannot exceed 30 characters")
    val companyNumber: String = "",

    @field:NotEmpty(message = "Name on invoice must not be empty")
    @field:Size(max = 255, message = "Name on invoice cannot exceed 255 characters")
    val nameOnInvoice: String = "",

    val address: AddressCreateRequest = AddressCreateRequest()
) : ValidatableRequest<CustomerB2BCreateRequest>() {
    constructor(customerB2B: CustomerB2B): this(
        customer = CustomerCreateRequest(customerB2B.customer),
        companyNumber = customerB2B.companyNumber,
        nameOnInvoice = customerB2B.nameOnInvoice,
        address = AddressCreateRequest(customerB2B.address)
    )
}
