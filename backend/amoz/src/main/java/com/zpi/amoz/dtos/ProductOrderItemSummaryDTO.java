package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductOrderItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące podsumowanie pozycji zamówienia, zawierające informacje o wariancie produktu, cenie jednostkowej oraz ilości.")
public record ProductOrderItemSummaryDTO(

        @Schema(description = "Identyfikator pozycji zamówienia", example = "a2e9f5c4-4d52-40b3-bf3b-ecbb94c79e7b")
        UUID productOrderItemId,

        @Schema(description = "Podsumowanie wariantu produktu",
                example = "{\"productVariantId\": \"4d8f33b6-826f-47d9-b0fc-e1a89c00bdb3\", \"color\": \"Czerwony\", \"size\": \"L\"}")
        ProductVariantSummaryDTO productVariant,

        @Schema(description = "Cena jednostkowa produktu", example = "299.99")
        BigDecimal unitPrice,

        @Schema(description = "Ilość zamówionych produktów", example = "2")
        int amount,

        @Schema(description = "Nazwa produktu (opcjonalnie)", nullable = true, example = "\"Koszulka Czerwona\"")
        Optional<String> productName

) {

    public static ProductOrderItemSummaryDTO toProductOrderItemSummaryDTO(ProductOrderItem productOrderItem) {
        return new ProductOrderItemSummaryDTO(
                productOrderItem.getProductOrderItemId(),
                productOrderItem.getProductVariant() != null ? ProductVariantSummaryDTO.toProductVariantSummaryDTO(productOrderItem.getProductVariant()) : null,
                productOrderItem.getUnitPrice(),
                productOrderItem.getAmount(),
                Optional.ofNullable(productOrderItem.getProductName())
        );
    }
}


