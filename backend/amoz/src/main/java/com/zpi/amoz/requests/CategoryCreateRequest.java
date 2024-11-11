package com.zpi.amoz.requests;

import jakarta.validation.constraints.NotBlank;

import java.util.Optional;
import java.util.UUID;

public record CategoryCreateRequest(
        @NotBlank(message = "Category name is required") String name,
        Optional<UUID> parentCategoryId
) {
}
