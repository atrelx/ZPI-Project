package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.UnitWeight;
import com.zpi.amoz.models.Weight;

import java.util.UUID;

public record WeightDTO(UUID weightId,
                        UnitWeight unitWeight,
                        Double amount) {

    public static WeightDTO toWeightDTO(Weight weight) {
        return new WeightDTO(
                weight.getWeightId(),
                weight.getUnitWeight(),
                weight.getAmount()
        );
    }
}

