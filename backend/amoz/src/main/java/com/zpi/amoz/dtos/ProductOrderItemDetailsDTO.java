package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductOrderItem;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące szczegóły pozycji zamówienia, zawierające informacje o wariancie produktu, cenie jednostkowej oraz ilości.")
public record ProductOrderItemDetailsDTO(

        @Schema(description = "Identyfikator pozycji zamówienia", example = "a2e9f5c4-4d52-40b3-bf3b-ecbb94c79e7b")
        UUID productOrderItemId,

        @Schema(description = "Szczegóły wariantu produktu", implementation = ProductVariantDetailsDTO.class)
        ProductVariantDetailsDTO productVariant,

        @Schema(description = "Cena jednostkowa produktu", example = "299.99")
        BigDecimal unitPrice,

        @Schema(description = "Ilość zamówionych produktów", example = "2")
        int amount,

        @Schema(description = "Nazwa produktu (opcjonalnie)", nullable = true, example = "\"Koszulka, Czerwona\"")
        String productName

) {

    public static ProductOrderItemDetailsDTO toProductOrderItemDetailsDTO(ProductOrderItem productOrderItem) {
        return new ProductOrderItemDetailsDTO(
                productOrderItem.getProductOrderItemId(),
                ProductVariantDetailsDTO.toProductVariantDetailsDTO(productOrderItem.getProductVariant()),
                productOrderItem.getUnitPrice(),
                productOrderItem.getAmount(),
                productOrderItem.getProductName()
        );
    }
}
