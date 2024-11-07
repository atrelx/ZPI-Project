package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ProductDTO(
        UUID productId,
        String name,
        BigDecimal price,
        CategoryDTO category,
        Optional<ProductVariantDTO> mainProductVariant,
        List<ProductAttributeDTO> productAttributes,
        Optional<String> description,
        Optional<String> brand,
        boolean isActive
) {
    public static ProductDTO toProductDTO(Product product) {
        return new ProductDTO(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                CategoryDTO.toCategoryDTO(product.getCategory()),
                Optional.ofNullable(product.getMainProductVariant() != null ? ProductVariantDTO.toProductVariantDTO(product.getMainProductVariant()) : null),
                product.getProductAttributes() != null ? product.getProductAttributes().stream()
                        .map(ProductAttributeDTO::toProductAttributeDTO).toList() : List.of(),
                Optional.ofNullable(product.getDescription()),
                Optional.ofNullable(product.getBrand()),
                product.isActive()
        );
    }
}
