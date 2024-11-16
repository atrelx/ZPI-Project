package com.example.amoz.models

import com.example.amoz.enums.UnitWeight
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Weight(
    @Contextual val weightId: UUID,
    val unitWeight: UnitWeight,
    val amount: Double
)
