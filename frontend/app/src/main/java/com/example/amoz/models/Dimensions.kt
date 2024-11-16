package com.example.amoz.models

import com.example.amoz.enums.UnitDimensions
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Dimensions(
    @Contextual val dimensionsId: UUID,
    val unitDimensions: UnitDimensions,
    val height: Double,
    val length: Double,
    val width: Double
)
