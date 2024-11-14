package com.zpi.amoz.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

@Schema(description = "Request do tworzenia informacji o stanie magazynowym produktu, zawierający dane dotyczące dostępnych ilości oraz ewentualnej ilości alarmowej.")
public record StockCreateRequest(

        @Schema(description = "Ilość dostępna w magazynie", example = "100")
        @NotNull(message = "Amount available is required")
        @Min(value = 0, message = "Amount available must be greater than or equal to 0")
        Integer amountAvailable,

        @Schema(description = "Ilość alarmowa, poniżej której stan magazynowy wymaga uzupełnienia", example = "10", nullable = true)
        Optional<@Min(value = 0, message = "Alarming amount must be greater than or equal to 0") Integer> alarmingAmount
) {
}
