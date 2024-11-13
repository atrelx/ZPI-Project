package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductVariant;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductVariantSummaryDTO(UUID productVariantId,
                                       Integer code,
                                       BigDecimal variantPrice,
                                       String variantName) {

    public static ProductVariantSummaryDTO toProductVariantSummaryDTO(ProductVariant productVariant) {
        return new ProductVariantSummaryDTO(
                productVariant.getProductVariantId(),
                productVariant.getCode(),
                productVariant.getVariantPrice(),
                productVariant.getVariantName()
        );
    }
}

