package com.example.amoz.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class Stock(
    @Contextual val stockId: UUID,
    val amountAvailable: Int,
    val alarmingAmount: Int? = null,
    val isAlarmTriggered: Boolean
)
