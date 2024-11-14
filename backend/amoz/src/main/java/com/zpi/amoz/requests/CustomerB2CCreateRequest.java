package com.zpi.amoz.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request do tworzenia klienta B2C, zawierający dane klienta oraz dane osobowe klienta.")
public record CustomerB2CCreateRequest(

        @Schema(description = "Sekcja klienta, zawierająca dane klienta", implementation = CustomerCreateRequest.class)
        @NotNull(message = "Customer section must be filled")
        CustomerCreateRequest customer,

        @Schema(description = "Dane osobowe klienta", required = true, implementation = PersonCreateRequest.class)
        @NotNull(message = "Customer's personal data must be given")
        PersonCreateRequest person

) {
}

