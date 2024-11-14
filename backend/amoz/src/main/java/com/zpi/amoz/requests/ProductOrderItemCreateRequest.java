package com.zpi.amoz.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.util.UUID;

@Schema(description = "Request do tworzenia elementu zamówienia produktu, zawierający identyfikator wariantu produktu oraz ilość.")
public record ProductOrderItemCreateRequest(

        @Schema(description = "Identyfikator wariantu produktu, który jest częścią zamówienia", example = "e7e7d0ff-64a4-45f1-929b-e7e0d6e8e4b5")
        @NotNull(message = "Product variant ID is required")
        UUID productVariantId,

        @Schema(description = "Ilość zamawianych produktów", example = "2")
        @NotNull(message = "Amount is required")
        @Min(value = 1, message = "Amount must be at least 1")
        Integer amount
) {
}
