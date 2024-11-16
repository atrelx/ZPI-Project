package com.example.amoz.requests

import jakarta.validation.constraints.Min
import kotlinx.serialization.Serializable

@Serializable
data class StockCreateRequest(

    @field:Min(value = 0, message = "Amount available must be greater than or equal to 0")
    val amountAvailable: Int,

    @field:Min(value = 0, message = "Alarming amount must be greater than or equal to 0")
    val alarmingAmount: Int? = null
)
