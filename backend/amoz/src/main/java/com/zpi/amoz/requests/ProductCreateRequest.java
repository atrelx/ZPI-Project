package com.zpi.amoz.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Schema(description = "Request do tworzenia nowego produktu w systemie, zawierający wszystkie niezbędne dane takie jak nazwa, cena, kategoria, opis, marka, warianty i atrybuty.")
public record ProductCreateRequest(

        @Schema(description = "Nazwa produktu", example = "Czarny T-shirt")
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @Schema(description = "Cena produktu", example = "199.99")
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Price must be a valid amount with up to 10 digits and 2 decimal places")
        BigDecimal price,

        @Schema(description = "Identyfikator kategorii, do której należy produkt", example = "e7e7d0ff-64a4-45f1-929b-e7e0d6e8e4b5")
        @NotNull(message = "Category ID is required")
        UUID categoryId,

        @Schema(description = "Opis produktu", example = "Wygodny czarny t-shirt wykonany z wysokiej jakości bawełny.", nullable = true)
        Optional<@Size(max = 1000, message = "Description cannot exceed 1000 characters") String> description,

        @Schema(description = "Marka produktu", example = "Nike", nullable = true)
        Optional<@Size(max = 50, message = "Brand cannot exceed 50 characters") String> brand,

        @Schema(description = "Lista identyfikatorów wariantów produktu", example = "[\"e7e7d0ff-64a4-45f1-929b-e7e0d6e8e4b5\"]")
        List<UUID> productVariantIds,

        @Schema(description = "Lista atrybutów produktu")
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
