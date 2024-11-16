package com.zpi.amoz.dtos;

import com.zpi.amoz.models.VariantAttribute;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące atrybut wariantu produktu, zawierające informacje o atrybucie i jego wartości.")
public record VariantAttributeDTO(

        @Schema(description = "Identyfikator atrybutu wariantu", example = "b2c3248d-f91b-4a34-9d73-e4f1d0d902c3")
        UUID variantAttributeId,

        @Schema(description = "Atrybut związany z wariantem produktu", implementation = AttributeDTO.class)
        AttributeDTO attribute,

        @Schema(description = "Wartość atrybutu wariantu, jeśli istnieje", example = "Czerwony")
        Optional<String> value

) {

    public static VariantAttributeDTO toVariantAttributeDTO(VariantAttribute variantAttribute) {
        return new VariantAttributeDTO(
                variantAttribute.getVariantAttributeId(),
                AttributeDTO.toAttributeDTO(variantAttribute.getAttribute()),
                Optional.ofNullable(variantAttribute.getValue())
        );
    }
}


