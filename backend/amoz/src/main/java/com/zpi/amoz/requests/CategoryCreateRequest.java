package com.zpi.amoz.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;
import java.util.UUID;

@Schema(description = "Request do tworzenia nowej kategorii, zawierający nazwę kategorii oraz opcjonalny identyfikator nadrzędnej kategorii.")
public record CategoryCreateRequest(

        @Schema(description = "Nazwa kategorii", example = "Odzież")
        @NotBlank(message = "Category name is required")
        String name,

        @Schema(description = "Identyfikator nadrzędnej kategorii, jeśli istnieje", example = "d4318c33-12c8-4d6c-bf50-f48d14f54b64", nullable = true)
        Optional<UUID> parentCategoryId

) {
}
