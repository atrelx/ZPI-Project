package com.example.amoz.api.requests

import com.example.amoz.enums.UnitWeight
import com.example.amoz.interfaces.ValidatableRequest
import javax.validation.constraints.Positive
import kotlinx.serialization.Serializable

@Serializable
data class WeightCreateRequest(
    @field:Positive(message = "Unit weight must be greater than 0")
    val unitWeight: UnitWeight,

    @field:Positive(message = "Amount must be greater than 0")
    val amount: Double
) : ValidatableRequest<WeightCreateRequest>()
