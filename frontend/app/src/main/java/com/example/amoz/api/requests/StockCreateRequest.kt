package com.example.amoz.api.requests

import com.example.amoz.interfaces.ValidatableRequest
import javax.validation.constraints.Min

import kotlinx.serialization.Serializable

@Serializable
data class StockCreateRequest(

    @field:Min(value = 0, message = "Amount available must be greater than or equal to 0")
    val amountAvailable: Int,

    @field:Min(value = 0, message = "Alarming amount must be greater than or equal to 0")
    val alarmingAmount: Int? = null
) : ValidatableRequest<StockCreateRequest>()
