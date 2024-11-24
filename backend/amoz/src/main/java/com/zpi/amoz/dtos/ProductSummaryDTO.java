package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące podsumowanie produktu, zawierające podstawowe informacje o produkcie, takie jak cena, kategoria, wariant oraz opis.")
public record ProductSummaryDTO(

        @Schema(description = "Identyfikator produktu", example = "d4318c33-12c8-4d6c-bf50-f48d14f54b64")
        UUID productId,

        @Schema(description = "Nazwa produktu", example = "Koszulka Męska")
        String name,

        @Schema(description = "Cena produktu", example = "99.99")
        BigDecimal price,

        @Schema(description = "Kategoria produktu", implementation = CategorySummaryDTO.class, nullable = true)
        Optional<CategorySummaryDTO> category,

        @Schema(description = "Główny wariant produktu", nullable = true, implementation = ProductVariantDetailsDTO.class)
        Optional<ProductVariantDetailsDTO> mainProductVariant,

        @Schema(description = "Opis produktu", nullable = true, example = "Wygodna koszulka męska, idealna do codziennego użytku.")
        Optional<String> description,

        @Schema(description = "Marka produktu", nullable = true, example = "Nike")
        Optional<String> brand

) {

    public static ProductSummaryDTO toProductSummaryDTO(Product product) {
        return new ProductSummaryDTO(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                Optional.ofNullable(product.getCategory() != null ? CategorySummaryDTO.toCategorySummaryDTO(product.getCategory()) : null),
                Optional.ofNullable(product.getMainProductVariant() != null
                        ? ProductVariantDetailsDTO.toProductVariantDetailsDTO(product.getMainProductVariant())
                        : null),
                Optional.ofNullable(product.getDescription()),
                Optional.ofNullable(product.getBrand())
        );
    }
}

