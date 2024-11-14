package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductAttribute;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące atrybut produktu, w tym jego identyfikator, wartość oraz powiązany atrybut.")
public record ProductAttributeDTO(

        @Schema(description = "Identyfikator atrybutu produktu", example = "d1f3d4a5-bf13-4a74-96f7-cf314f964b5a")
        UUID productAttributeId,

        @Schema(description = "Atrybut powiązany z produktem", example = "{\"attributeName\": \"Kolor\"}")
        AttributeDTO attribute,

        @Schema(description = "Wartość atrybutu produktu, jeśli istnieje", nullable = true, example = "\"Czerwony\"")
        Optional<String> value

) {

    public static ProductAttributeDTO toProductAttributeDTO(ProductAttribute productAttribute) {
        return new ProductAttributeDTO(
                productAttribute.getProductAttributeId(),
                AttributeDTO.toAttributeDTO(productAttribute.getAttribute()),
                Optional.ofNullable(productAttribute.getValue())
        );
    }
}


