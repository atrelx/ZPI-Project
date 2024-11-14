package com.zpi.amoz.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request do tworzenia klienta B2B, zawierający dane klienta, numer firmy, dane do faktury oraz adres.")
public record CustomerB2BCreateRequest(

        @Schema(description = "Sekcja klienta, zawierająca dane klienta", implementation = CustomerCreateRequest.class)
        @NotNull(message = "Customer section must be filled")
        CustomerCreateRequest customer,

        @Schema(description = "Numer identyfikacyjny firmy", example = "1234567890")
        @NotEmpty(message = "Company number must not be empty")
        @Size(max = 30, message = "Company number cannot exceed 30 characters")
        String companyNumber,

        @Schema(description = "Nazwa na fakturze", example = "ABC Corp.")
        @NotEmpty(message = "Name on invoice must not be empty")
        @Size(max = 255, message = "Name on invoice cannot exceed 255 characters")
        String nameOnInvoice,

        @Schema(description = "Adres firmy", implementation = AddressCreateRequest.class)
        @NotNull(message = "Company's address must be given")
        AddressCreateRequest address

) {
}
