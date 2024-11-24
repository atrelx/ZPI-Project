package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductVariant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące skrócone informacje o wariancie produktu, w tym identyfikator, cenę oraz nazwę wariantu.")
public record ProductVariantSummaryDTO(

        @Schema(description = "Identyfikator wariantu produktu", example = "e7e7d0ff-64a4-45f1-929b-e7e0d6e8e4b5")
        UUID productVariantId,

        @Schema(description = "Kod identyfikacyjny wariantu produktu", example = "12345")
        Integer code,

        @Schema(description = "Cena wariantu produktu", example = "199.99")
        BigDecimal variantPrice,

        @Schema(description = "Nazwa wariantu produktu", example = "Czarny T-shirt, rozmiar M")
        Optional<String> variantName

) {

    public static ProductVariantSummaryDTO toProductVariantSummaryDTO(ProductVariant productVariant) {
        return new ProductVariantSummaryDTO(
                productVariant.getProductVariantId(),
                productVariant.getCode(),
                productVariant.getVariantPrice(),
                Optional.ofNullable(productVariant.getVariantName())
        );
    }
}


