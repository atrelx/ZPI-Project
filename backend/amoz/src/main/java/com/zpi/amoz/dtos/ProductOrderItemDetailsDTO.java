package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductOrderItem;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public record ProductOrderItemDetailsDTO(UUID productOrderItemId,
                                         ProductVariantDetailsDTO productVariant,
                                         BigDecimal unitPrice,
                                         int amount,
                                         Optional<String> productName) {

    public static ProductOrderItemDetailsDTO toProductOrderItemDetailsDTO(ProductOrderItem productOrderItem) {
        return new ProductOrderItemDetailsDTO(
                productOrderItem.getProductOrderItemId(),
                productOrderItem.getProductVariant() != null ? ProductVariantDetailsDTO.toProductVariantDetailsDTO(productOrderItem.getProductVariant()) : null,
                productOrderItem.getUnitPrice(),
                productOrderItem.getAmount(),
                Optional.ofNullable(productOrderItem.getProductName())
        );
    }
}
