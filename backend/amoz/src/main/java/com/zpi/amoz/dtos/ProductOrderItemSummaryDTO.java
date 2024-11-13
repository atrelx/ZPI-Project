package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductOrderItem;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public record ProductOrderItemSummaryDTO(UUID productOrderItemId,
                                         ProductVariantSummaryDTO productVariant,
                                         BigDecimal unitPrice,
                                         int amount,
                                         Optional<String> productName) {

    public static ProductOrderItemSummaryDTO toProductOrderItemSummaryDTO(ProductOrderItem productOrderItem) {
        return new ProductOrderItemSummaryDTO(
                productOrderItem.getProductOrderItemId(),
                productOrderItem.getProductVariant() != null ? ProductVariantSummaryDTO.toProductVariantSummaryDTO(productOrderItem.getProductVariant()) : null,
                productOrderItem.getUnitPrice(),
                productOrderItem.getAmount(),
                Optional.ofNullable(productOrderItem.getProductName())
        );
    }
}

