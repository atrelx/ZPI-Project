package com.zpi.amoz.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerB2CCreateRequest(
        @NotNull(message = "Customer section must be filled")
        CustomerCreateRequest customer,

        @NotNull(message = "Customer's personal data must be given")
        PersonCreateRequest person
        ) {
}
