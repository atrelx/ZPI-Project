package com.zpi.amoz.dtos;


import com.zpi.amoz.models.Attribute;

import java.util.UUID;

public record AttributeDTO(
        String attributeName
) {
    public static AttributeDTO toAttributeDTO(Attribute attribute) {
        return new AttributeDTO(
                attribute.getAttributeName()
        );
    }
}
