package com.zpi.amoz.requests;

import com.zpi.amoz.enums.UnitDimensions;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request do tworzenia wymiarów jednostkowych produktu, zawierający jednostkę wagi, wysokość, długość i szerokość.")
public record DimensionsCreateRequest(

        @Schema(description = "Jednostka wagi produktu", example = "KG")
        @NotNull(message = "Unit weight is required")
        @Positive(message = "Unit weight must be greater than 0")
        UnitDimensions unitDimensions,

        @Schema(description = "Wysokość produktu", example = "20.5")
        @NotNull(message = "Height is required")
        @Positive(message = "Height must be greater than 0")
        Double height,

        @Schema(description = "Długość produktu", example = "30.0")
        @NotNull(message = "Length is required")
        @Positive(message = "Length must be greater than 0")
        Double length,

        @Schema(description = "Szerokość produktu", example = "10.0")
        @NotNull(message = "Width is required")
        @Positive(message = "Width must be greater than 0")
        Double width
) {
}
