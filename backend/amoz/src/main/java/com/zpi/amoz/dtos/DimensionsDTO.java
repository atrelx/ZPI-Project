package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.UnitDimensions;
import com.zpi.amoz.models.Dimensions;

import java.util.Optional;
import java.util.UUID;

public record DimensionsDTO(
        UUID dimensionsId,
        UnitDimensions unitDimensions,
        Optional<Double> height,
        Optional<Double> length,
        Optional<Double> width
) {
    public static DimensionsDTO toDimensionsDTO(Dimensions dimensions) {
        return new DimensionsDTO(
                dimensions.getDimensionsId(),
                dimensions.getUnitDimensions(),
                Optional.ofNullable(dimensions.getHeight()),
                Optional.ofNullable(dimensions.getLength()),
                Optional.ofNullable(dimensions.getWidth())
        );
    }
}

