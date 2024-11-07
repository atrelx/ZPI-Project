package com.zpi.amoz.dtos;

import com.zpi.amoz.models.VariantAttribute;

import java.util.Optional;
import java.util.UUID;

public record VariantAttributeDTO(UUID variantAttributeId,
                                  AttributeDTO attribute,
                                  Optional<String> value) {

    public static VariantAttributeDTO toVariantAttributeDTO(VariantAttribute variantAttribute) {
        return new VariantAttributeDTO(
                variantAttribute.getVariantAttributeId(),
                AttributeDTO.toAttributeDTO(variantAttribute.getAttribute()),
                Optional.ofNullable(variantAttribute.getValue())
        );
    }
}
