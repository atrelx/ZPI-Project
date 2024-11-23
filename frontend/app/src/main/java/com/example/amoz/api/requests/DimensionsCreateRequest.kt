package com.example.amoz.api.requests

import com.example.amoz.api.enums.UnitDimensions
import com.example.amoz.interfaces.ValidatableRequest
import kotlinx.serialization.Serializable
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Serializable
data class DimensionsCreateRequest(
    @field:Positive(message = "Unit weight must be greater than 0")
    val unitDimensions: UnitDimensions,

    @field:Positive(message = "Height must be greater than 0")
    val height: Double,

    @field:Positive(message = "Length must be greater than 0")
    val length: Double,

    @field:Positive(message = "Width must be greater than 0")
    val width: Double
) : ValidatableRequest<DimensionsCreateRequest>()
