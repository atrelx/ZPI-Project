package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Product;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public record ProductSummaryDTO(
        UUID productId,
        String name,
        BigDecimal price,
        CategorySummaryDTO category,
        Optional<ProductVariantDetailsDTO> mainProductVariant,
        Optional<String> description,
        Optional<String> brand
) {
    public static ProductSummaryDTO toProductSummaryDTO(Product product) {
        return new ProductSummaryDTO(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                CategorySummaryDTO.toCategorySummaryDTO(product.getCategory()),
                Optional.ofNullable(product.getMainProductVariant() != null ? ProductVariantDetailsDTO.toProductVariantDetailsDTO(product.getMainProductVariant()) : null),
                Optional.ofNullable(product.getDescription()),
                Optional.ofNullable(product.getBrand())
        );
    }
}