package com.zpi.amoz.requests;

import com.zpi.amoz.enums.UnitWeight;

import jakarta.validation.constraints.*;

public record WeightCreateRequest(
        @NotNull(message = "Unit weight is required")
        @Positive(message = "Unit weight must be greater than 0")
        UnitWeight unitWeight,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than 0")
        Double amount
) {
}
