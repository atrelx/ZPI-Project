package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductVariant;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductVariantDetailsDTO(UUID productVariantId,
                                       Integer code,
                                       StockDTO stock,
                                       DimensionsDTO dimensions,
                                       WeightDTO weight,
                                       BigDecimal variantPrice,
                                       String variantName,
                                       List<VariantAttributeDTO> variantAttributes) {

    public static ProductVariantDetailsDTO toProductVariantDetailsDTO(ProductVariant productVariant) {
        return new ProductVariantDetailsDTO(
                productVariant.getProductVariantId(),
                productVariant.getCode(),
                productVariant.getStock() != null ? StockDTO.toStockDTO(productVariant.getStock()) : null,
                productVariant.getDimensions() != null ? DimensionsDTO.toDimensionsDTO(productVariant.getDimensions()) : null,
                productVariant.getWeight() != null ? WeightDTO.toWeightDTO(productVariant.getWeight()) : null,
                productVariant.getVariantPrice(),
                productVariant.getVariantName(),
                productVariant.getVariantAttributes() != null ? productVariant.getVariantAttributes().stream()
                        .map(VariantAttributeDTO::toVariantAttributeDTO).toList() : List.of()
        );
    }
}
