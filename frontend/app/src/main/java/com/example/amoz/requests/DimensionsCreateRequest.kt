package com.example.amoz.requests

import com.example.amoz.enums.UnitDimensions
import kotlinx.serialization.Serializable
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

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
)
