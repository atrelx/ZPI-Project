package com.zpi.amoz.requests;

import com.zpi.amoz.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ProductOrderCreateRequest(
        @NotNull(message = "Status is required")
        Status status,

        @NotNull(message = "Product order items cannot be null")
        @Size(min = 1, message = "At least one product order item is required")
        List<ProductOrderItemCreateRequest> productOrderItems,

        Optional<AddressCreateRequest> address,

        Optional<UUID> customerId,

        Optional<@Size(max = 10, message = "Tracking number cannot exceed 10 characters") String> trackingNumber,

        Optional<@PastOrPresent(message = "Time of sending cannot be in the future") LocalDateTime> timeOfSending
) {
}
