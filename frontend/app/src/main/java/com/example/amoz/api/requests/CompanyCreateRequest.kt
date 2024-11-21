package com.example.amoz.api.requests

import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Company
import kotlinx.serialization.Serializable
import javax.validation.Validator

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Serializable
data class CompanyCreateRequest(

    @field:NotBlank(message = "Company number is required")
    @field:Size(max = 50, message = "Company number should not exceed 50 characters")
    var companyNumber: String = "",

    @field:NotBlank(message = "Country of registration is required")
    @field:Size(max = 100, message = "Country of registration should not exceed 100 characters")
    var countryOfRegistration: String = "",

    @field:NotBlank(message = "Company name is required")
    @field:Size(max = 100, message = "Company name should not exceed 100 characters")
    var name: String = "",

    var address: AddressCreateRequest = AddressCreateRequest(),

    var regon: String? = null
) : ValidatableRequest<CompanyCreateRequest>() {
    constructor(company: Company) : this(
        companyNumber = company.companyNumber,
        countryOfRegistration = company.countryOfRegistration,
        name = company.name,
        address = AddressCreateRequest(company.address),
        regon = company.regon
    )
}


