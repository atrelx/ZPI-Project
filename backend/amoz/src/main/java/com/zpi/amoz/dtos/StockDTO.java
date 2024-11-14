package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Stock;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące dane dotyczące stanu magazynowego, w tym dostępność oraz alarmy magazynowe.")
public record StockDTO(

        @Schema(description = "Identyfikator stanu magazynowego", example = "b1e7c08f-21b1-44f7-bb73-5d9b53c7607e")
        UUID stockId,

        @Schema(description = "Dostępna ilość produktów w magazynie", example = "100")
        Integer amountAvailable,

        @Schema(description = "Ilość progowa, po której przekroczeniu uruchamiany jest alarm (jeśli istnieje)", example = "20", nullable = true)
        Optional<Integer> alarmingAmount,

        @Schema(description = "Wskaźnik, czy alarm został uruchomiony", example = "true")
        boolean isAlarmTriggered

) {

    public static StockDTO toStockDTO(Stock stock) {
        return new StockDTO(
                stock.getStockId(),
                stock.getAmountAvailable(),
                Optional.ofNullable(stock.getAlarmingAmount()),
                stock.isAlarmTriggered()
        );
    }
}

