package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Product;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące szczegóły produktu, w tym informacje o kategorii, wariantach i atrybutach produktu.")
public record ProductDetailsDTO(

        @Schema(description = "Identyfikator produktu", example = "b88aef3c-7c70-4c33-9980-d96e6849a0ea")
        UUID productId,

        @Schema(description = "Nazwa produktu", example = "Smartphone XYZ")
        String name,

        @Schema(description = "Cena produktu", example = "1999.99")
        BigDecimal price,

        @Schema(description = "Kategoria produktu", implementation = CategoryDetailsDTO.class)
        CategoryDetailsDTO category,

        @Schema(description = "Główny wariant produktu", nullable = true, implementation = ProductVariantDetailsDTO.class)
        Optional<ProductVariantDetailsDTO> mainProductVariant,

        @ArraySchema(schema = @Schema(description = "Lista atrybutów produktu", implementation = ProductAttributeDTO.class))
        List<ProductAttributeDTO> productAttributes,

        @Schema(description = "Opis produktu", nullable = true, example = "Nowoczesny smartphone z ekranem OLED, 128 GB pamięci wewnętrznej.")
        Optional<String> description,

        @Schema(description = "Marka produktu", nullable = true, example = "XYZ Electronics")
        Optional<String> brand

) {

    public static ProductDetailsDTO toProductDetailsDTO(Product product) {
        return new ProductDetailsDTO(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                CategoryDetailsDTO.toCategoryDTO(product.getCategory()),
                Optional.ofNullable(product.getMainProductVariant() != null ? ProductVariantDetailsDTO.toProductVariantDetailsDTO(product.getMainProductVariant()) : null),
                product.getProductAttributes() != null ? product.getProductAttributes().stream()
                        .map(ProductAttributeDTO::toProductAttributeDTO).toList() : List.of(),
                Optional.ofNullable(product.getDescription()),
                Optional.ofNullable(product.getBrand())
        );
    }
}

