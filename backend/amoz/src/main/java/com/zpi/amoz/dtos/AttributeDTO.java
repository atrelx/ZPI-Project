package com.zpi.amoz.dtos;


import com.zpi.amoz.models.Attribute;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Obiekt reprezentujący atrybut z jego nazwą")
public record AttributeDTO(

        @Schema(description = "Nazwa atrybutu", example = "Kolor")
        String attributeName
) {
    public static AttributeDTO toAttributeDTO(Attribute attribute) {
        return new AttributeDTO(
                attribute.getAttributeName()
        );
    }
}

