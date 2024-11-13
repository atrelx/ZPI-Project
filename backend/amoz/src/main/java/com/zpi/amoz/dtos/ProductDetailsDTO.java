package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ProductDetailsDTO(
        UUID productId,
        String name,
        BigDecimal price,
        CategoryDTO category,
        Optional<ProductVariantDetailsDTO> mainProductVariant,
        List<ProductAttributeDTO> productAttributes,
        Optional<String> description,
        Optional<String> brand
) {
    public static ProductDetailsDTO toProductDetailsDTO(Product product) {
        return new ProductDetailsDTO(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                CategoryDTO.toCategoryDTO(product.getCategory()),
                Optional.ofNullable(product.getMainProductVariant() != null ? ProductVariantDetailsDTO.toProductVariantDetailsDTO(product.getMainProductVariant()) : null),
                product.getProductAttributes() != null ? product.getProductAttributes().stream()
                        .map(ProductAttributeDTO::toProductAttributeDTO).toList() : List.of(),
                Optional.ofNullable(product.getDescription()),
                Optional.ofNullable(product.getBrand())
        );
    }
}
