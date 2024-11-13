package com.zpi.amoz.requests;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.constraints.*;

public record ProductVariantCreateRequest(
        @NotNull(message = "Product ID is required")
        UUID productID,

        @Positive(message = "Product variant code must be a positive number")
        @NotNull(message = "Product variant code is required")
        int productVariantCode,

        @NotNull(message = "Stock information is required")
        StockCreateRequest stock,

        Optional<WeightCreateRequest> weight,

        Optional<DimensionsCreateRequest> dimensions,

        @NotNull(message = "Variant attributes cannot be null")
        List<AttributeCreateRequest> variantAttributes,


        Optional<@Size(max = 100, message = "Variant name cannot exceed 100 characters") String> variantName,

        Optional<
                @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
                @Digits(integer = 10, fraction = 2, message = "Price must be a valid amount with up to 10 digits and 2 decimal places")
                        BigDecimal> variantPrice
) {
}

