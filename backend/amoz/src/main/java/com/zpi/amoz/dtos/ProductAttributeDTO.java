package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductAttribute;

import java.util.Optional;
import java.util.UUID;

public record ProductAttributeDTO(UUID productAttributeId,
                                  AttributeDTO attribute,
                                  Optional<String> value) {

    public static ProductAttributeDTO toProductAttributeDTO(ProductAttribute productAttribute) {
        return new ProductAttributeDTO(
                productAttribute.getProductAttributeId(),
                AttributeDTO.toAttributeDTO(productAttribute.getAttribute()),
                Optional.ofNullable(productAttribute.getValue())
        );
    }
}

