package com.zpi.amoz.dtos;


import com.zpi.amoz.enums.Status;
import com.zpi.amoz.models.ProductOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące podsumowanie zamówienia, zawierające ogólne informacje o zamówieniu oraz przykładowe pozycje zamówienia.")
public record ProductOrderSummaryDTO(

        @Schema(description = "Identyfikator zamówienia", example = "2a93d8f9-8467-4d43-b8fc-16b650d9a684")
        UUID productOrderId,

        @Schema(description = "Status zamówienia", example = "ORDERED")
        Status status,

        @Schema(description = "Przykładowa pozycja zamówienia", implementation = ProductOrderItemSummaryDTO.class)
        ProductOrderItemSummaryDTO sampleProductOrderItem,

        @Schema(description = "Łączna kwota do zapłaty za zamówienie", example = "199.98")
        BigDecimal totalDue,

        @Schema(description = "Numer śledzenia przesyłki, jeśli dostępny", nullable = true, example = "\"ABC123456789\"")
        Optional<String> trackingNumber,

        @Schema(description = "Data wysłania zamówienia, jeśli dostępna", nullable = true, example = "2024-11-14T15:30:00")
        Optional<LocalDateTime> timeOfSending,

        @Schema(description = "Data utworzenia zamówienia", example = "2024-11-01T10:00:00")
        LocalDateTime timeOfCreation

) {

    public static ProductOrderSummaryDTO toProductOrderSummaryDTO(ProductOrder productOrder) {
        return new ProductOrderSummaryDTO(
                productOrder.getProductOrderId(),
                productOrder.getStatus(),
                ProductOrderItemSummaryDTO.toProductOrderItemSummaryDTO(productOrder.getOrderItems().get(0)),
                productOrder.getTotalDue(),
                Optional.ofNullable(productOrder.getTrackingNumber()),
                Optional.ofNullable(productOrder.getTimeOfSending()),
                productOrder.getTimeOfCreation()
        );
    }
}


