package com.zpi.amoz.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.util.UUID;

public record ProductOrderItemCreateRequest(
        @NotNull(message = "Product variant ID is required")
        UUID productVariantId,

        @NotNull(message = "Amount is required")
        @Min(value = 1, message = "Amount must be at least 1")
        Integer amount
) {
}
