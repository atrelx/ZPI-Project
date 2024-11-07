package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ProductVariant;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductVariantDTO(UUID productVariantId,
                                Integer code,
                                StockDTO stock,
                                DimensionsDTO dimensions,
                                WeightDTO weight,
                                BigDecimal impactOnPrice,
                                String variantName,
                                boolean isActive,
                                List<VariantAttributeDTO> variantAttributes) {

    public static ProductVariantDTO toProductVariantDTO(ProductVariant productVariant) {
        return new ProductVariantDTO(
                productVariant.getProductVariantId(),
                productVariant.getCode(),
                productVariant.getStock() != null ? StockDTO.toStockDTO(productVariant.getStock()) : null,
                productVariant.getDimensions() != null ? DimensionsDTO.toDimensionsDTO(productVariant.getDimensions()) : null,
                productVariant.getWeight() != null ? WeightDTO.toWeightDTO(productVariant.getWeight()) : null,
                productVariant.getImpactOnPrice(),
                productVariant.getVariantName(),
                productVariant.isActive(),
                productVariant.getVariantAttributes() != null ? productVariant.getVariantAttributes().stream()
                        .map(VariantAttributeDTO::toVariantAttributeDTO).toList() : List.of()
        );
    }
}
