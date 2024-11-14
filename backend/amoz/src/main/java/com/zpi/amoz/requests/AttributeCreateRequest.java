package com.zpi.amoz.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

@Schema(description = "Request do tworzenia nowego atrybutu, który zawiera nazwę atrybutu oraz wartość opcjonalną.")
public record AttributeCreateRequest(

        @Schema(description = "Nazwa atrybutu", example = "Kolor")
        @NotBlank(message = "Attribute name is required")
        @Size(max = 50, message = "Attribute name should not exceed 50 characters")
        String attributeName,

        @Schema(description = "Wartość atrybutu, jeśli istnieje", example = "Czerwony", nullable = true)
        Optional<@Size(max = 255) String> value

) {

    public AttributeCreateRequest(String attributeName, Optional<String> value) {
        this.attributeName = attributeName;
        this.value = value.isPresent() ? value : Optional.empty();
    }
}
