package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductOrderItem;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public record ProductOrderItemDTO(UUID productOrderItemId,
                                  ProductVariantDTO productVariant,
                                  BigDecimal unitPrice,
                                  int amount,
                                  Optional<String> productName) {

    public static ProductOrderItemDTO toProductOrderItemDTO(ProductOrderItem productOrderItem) {
        return new ProductOrderItemDTO(
                productOrderItem.getProductOrderItemId(),
                productOrderItem.getProductVariant() != null ? ProductVariantDTO.toProductVariantDTO(productOrderItem.getProductVariant()) : null,
                productOrderItem.getUnitPrice(),
                productOrderItem.getAmount(),
                Optional.ofNullable(productOrderItem.getProductName())
        );
    }
}
