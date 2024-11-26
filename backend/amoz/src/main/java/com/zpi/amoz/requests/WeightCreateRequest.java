package com.zpi.amoz.requests;

import com.zpi.amoz.enums.UnitWeight;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Request do tworzenia informacji o wadze wariantu produktu, zawierający jednostkę wagi oraz ilość.")
public record WeightCreateRequest(

        @Schema(description = "Jednostka wagi, która jest wymagana")
        @NotNull(message = "Unit weight is required")
        UnitWeight unitWeight,

        @Schema(description = "Ilość wagi, która musi być większa niż 0")
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than 0")
        Double amount
) {
}
