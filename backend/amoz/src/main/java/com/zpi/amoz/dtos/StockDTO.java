package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Stock;

import java.util.Optional;
import java.util.UUID;

public record StockDTO(UUID stockId,
                       Integer amountAvailable,
                       Optional<Integer> alarmingAmount,
                       boolean isAlarmTriggered) {

    public static StockDTO toStockDTO(Stock stock) {
        return new StockDTO(
                stock.getStockId(),
                stock.getAmountAvailable(),
                Optional.ofNullable(stock.getAlarmingAmount()),
                stock.isAlarmTriggered()
        );
    }
}
