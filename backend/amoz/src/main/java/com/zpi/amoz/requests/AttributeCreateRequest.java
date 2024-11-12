package com.zpi.amoz.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

public record AttributeCreateRequest(
        @NotBlank(message = "Attribute name is required") @Size(max = 50) String attributeName,
        Optional<@Size(max = 255) String> value
) {
    public AttributeCreateRequest(String attributeName, Optional<String> value) {
        this.attributeName = attributeName;
        this.value = value.isPresent() ? value : Optional.empty();
    }
}
