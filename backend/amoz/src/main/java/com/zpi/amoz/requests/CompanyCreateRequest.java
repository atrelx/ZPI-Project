package com.zpi.amoz.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Optional;

@Schema(description = "Request do tworzenia nowej firmy, zawierający informacje o numerze firmy, kraju rejestracji, nazwie, adresie oraz opcjonalnym numerze REGON.")
public record CompanyCreateRequest(

        @Schema(description = "Numer identyfikacyjny firmy", example = "1234567890")
        @NotBlank(message = "Company number is required")
        @Size(max = 50, message = "Company number should not exceed 50 characters")
        String companyNumber,

        @Schema(description = "Kraj rejestracji firmy", example = "Polska")
        @NotBlank(message = "Country of registration is required")
        @Size(max = 100, message = "Country of registration should not exceed 100 characters")
        String countryOfRegistration,

        @Schema(description = "Nazwa firmy", example = "Firma XYZ")
        @NotBlank(message = "Company name is required")
        @Size(max = 100, message = "Company name should not exceed 100 characters")
        String name,

        @Schema(description = "Adres firmy", implementation = AddressCreateRequest.class)
        @NotNull(message = "Address is required")
        @Valid
        AddressCreateRequest address,

        @Schema(description = "Numer REGON firmy, jeśli istnieje", example = "12345678901234", nullable = true)
        Optional<@Size(max = 14) String> regon

) {
}
