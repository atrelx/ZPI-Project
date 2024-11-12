package com.zpi.amoz.requests;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;

public record ProductCreateRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Price must be a valid amount with up to 10 digits and 2 decimal places")
        BigDecimal price,

        @NotNull(message = "Category ID is required")
        UUID categoryId,


        Optional<@Size(max = 1000, message = "Description cannot exceed 1000 characters") String> description,

        Optional<@Size(max = 50, message = "Brand cannot exceed 50 characters") String> brand,

        List<UUID> productVariantIds,

        List<AttributeCreateRequest> productAttributes


) {
    public ProductCreateRequest(
            String name,
            BigDecimal price,
            UUID categoryId,
            Optional<String> description,
            Optional<String> brand,
            List<UUID> productVariantIds,
            List<AttributeCreateRequest> productAttributes
    ) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.description = description;
        this.brand = brand;
        this.productVariantIds = productVariantIds != null ? productVariantIds : List.of();
        this.productAttributes = productAttributes != null ? productAttributes : List.of();
    }
}

