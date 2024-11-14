package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Product;
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

        @Schema(description = "Kategoria produktu", example = "{\"categoryId\": \"f2b33cd3-bb12-45d7-85f7-27d1457dcb72\", \"name\": \"Elektronika\", \"categoryLevel\": 1}")
        CategoryDTO category,

        @Schema(description = "Główny wariant produktu", nullable = true, example = "{\"productVariantId\": \"c2a26e6c-63be-4c25-8be1-4c689f4f13a4\", \"variantName\": \"128GB - Black\"}")
        Optional<ProductVariantDetailsDTO> mainProductVariant,

        @Schema(description = "Lista atrybutów produktu", example = "[{\"productAttributeId\": \"01d33d0e-abc2-4d29-bb55-3a85e71bc8ad\", \"attribute\": {\"attributeName\": \"Kolor\"}, \"value\": \"Czarny\"}, {\"productAttributeId\": \"08f9b626-8769-4374-9d69-b48f0e14f7fe\", \"attribute\": {\"attributeName\": \"Wielkość ekranu\"}, \"value\": \"6.5 cali\"}]")
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
                CategoryDTO.toCategoryDTO(product.getCategory()),
                Optional.ofNullable(product.getMainProductVariant() != null ? ProductVariantDetailsDTO.toProductVariantDetailsDTO(product.getMainProductVariant()) : null),
                product.getProductAttributes() != null ? product.getProductAttributes().stream()
                        .map(ProductAttributeDTO::toProductAttributeDTO).toList() : List.of(),
                Optional.ofNullable(product.getDescription()),
                Optional.ofNullable(product.getBrand())
        );
    }
}

