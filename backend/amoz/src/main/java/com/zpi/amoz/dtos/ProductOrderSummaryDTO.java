package com.zpi.amoz.dtos;


import com.zpi.amoz.enums.Status;
import com.zpi.amoz.models.ProductOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record ProductOrderSummaryDTO(UUID productOrderId,
                                     Status status,
                                     ProductOrderItemSummaryDTO sampleProductOrderItem,
                                     BigDecimal totalDue,
                                     Optional<String> trackingNumber,
                                     Optional<LocalDateTime> timeOfSending,
                                     Optional<LocalDateTime> timeOfCreation) {

    public static ProductOrderSummaryDTO toProductOrderSummaryDTO(ProductOrder productOrder) {
        return new ProductOrderSummaryDTO(
                productOrder.getProductOrderId(),
                productOrder.getStatus(),
                ProductOrderItemSummaryDTO.toProductOrderItemSummaryDTO(productOrder.getOrderItems().get(0)),
                productOrder.getTotalDue(),
                Optional.ofNullable(productOrder.getTrackingNumber()),
                Optional.ofNullable(productOrder.getTimeOfSending()),
                Optional.ofNullable(productOrder.getTimeOfCreation())
        );
    }
}

