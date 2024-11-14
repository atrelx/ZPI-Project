package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.UnitDimensions;
import com.zpi.amoz.models.Dimensions;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące wymiary wariantu produktu, w tym jednostki miary oraz wysokość, długość i szerokość.")
public record DimensionsDTO(

        @Schema(description = "Identyfikator wymiarów", example = "d23a1f0e-bda4-4f27-a3b8-636b1c4d5b90")
        UUID dimensionsId,

        @Schema(description = "Jednostki miary, w których wyrażone są wymiary", example = "CM")
        UnitDimensions unitDimensions,

        @Schema(description = "Wysokość obiektu w jednostkach miary", example = "25.5")
        Double height,

        @Schema(description = "Długość obiektu w jednostkach miary", example = "40.0")
        Double length,

        @Schema(description = "Szerokość obiektu w jednostkach miary", example = "10.0")
        Double width
) {
    public static DimensionsDTO toDimensionsDTO(Dimensions dimensions) {
        return new DimensionsDTO(
                dimensions.getDimensionsId(),
                dimensions.getUnitDimensions(),
                dimensions.getHeight(),
                dimensions.getLength(),
                dimensions.getWidth()
        );
    }
}



