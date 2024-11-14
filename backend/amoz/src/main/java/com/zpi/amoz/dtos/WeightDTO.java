package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.UnitWeight;
import com.zpi.amoz.models.Weight;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO reprezentujące wagę produktu, w tym jednostkę miary i wartość wagi.")
public record WeightDTO(

        @Schema(description = "Identyfikator wagi", example = "d5b1fbe7-c0d5-42d2-b5f0-f6230c9b57f2")
        UUID weightId,

        @Schema(description = "Jednostka miary wagi", example = "KG")
        UnitWeight unitWeight,

        @Schema(description = "Wartość wagi produktu", example = "1.25")
        Double amount

) {

    public static WeightDTO toWeightDTO(Weight weight) {
        return new WeightDTO(
                weight.getWeightId(),
                weight.getUnitWeight(),
                weight.getAmount()
        );
    }
}


