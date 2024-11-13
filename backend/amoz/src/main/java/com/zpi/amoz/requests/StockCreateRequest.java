package com.zpi.amoz.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record StockCreateRequest(
        @NotNull(message = "Amount available is required")
        @Min(value = 0, message = "Amount available must be greater than or equal to 0")
        Integer amountAvailable,

        Optional<@Min(value = 0, message = "Alarming amount must be greater than or equal to 0") Integer> alarmingAmount
) {
}
