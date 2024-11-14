package com.zpi.amoz.requests;

import com.zpi.amoz.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Schema(description = "Request do tworzenia zamówienia produktu w systemie, zawierający informacje o statusie, elementach zamówienia, adresie, kliencie, numerze śledzenia oraz czasie wysyłki.")
public record ProductOrderCreateRequest(

        @Schema(description = "Status zamówienia", example = "NEW")
        @NotNull(message = "Status is required")
        Status status,

        @Schema(description = "Lista elementów zamówienia", example = "[{productId: \"e7e7d0ff-64a4-45f1-929b-e7e0d6e8e4b5\", quantity: 2}]")
        @NotNull(message = "Product order items cannot be null")
        @Size(min = 1, message = "At least one product order item is required")
        List<ProductOrderItemCreateRequest> productOrderItems,

        @Schema(description = "Adres dostawy, jeśli istnieje", example = "{city: \"Warsaw\", street: \"Main St\", postalCode: \"00-001\"}")
        Optional<AddressCreateRequest> address,

        @Schema(description = "Identyfikator klienta, jeśli istnieje", example = "e7e7d0ff-64a4-45f1-929b-e7e0d6e8e4b5", nullable = true)
        Optional<UUID> customerId,

        @Schema(description = "Numer śledzenia przesyłki, jeśli dostępny", example = "ABC1234567", nullable = true)
        Optional<@Size(max = 10, message = "Tracking number cannot exceed 10 characters") String> trackingNumber,

        @Schema(description = "Czas wysyłki, jeśli dostępny", example = "2024-11-01T14:30:00", implementation = LocalDateTime.class, nullable = true)
        Optional<@PastOrPresent(message = "Time of sending cannot be in the future") LocalDateTime> timeOfSending

) {
}
