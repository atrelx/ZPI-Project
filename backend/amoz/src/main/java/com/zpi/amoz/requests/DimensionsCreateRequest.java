package com.zpi.amoz.requests;

import com.zpi.amoz.enums.UnitDimensions;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DimensionsCreateRequest(
        @NotNull(message = "Unit weight is required")
        @Positive(message = "Unit weight must be greater than 0")
        UnitDimensions unitDimensions,

        @NotNull(message = "Height is required")
        @Positive(message = "Height must be greater than 0")
        Double height,

        @NotNull(message = "Length is required")
        @Positive(message = "Length must be greater than 0")
        Double length,

        @NotNull(message = "Width is required")
        @Positive(message = "Width must be greater than 0")
        Double width
) {
}

