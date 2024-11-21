package com.zpi.amoz.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

@Schema(description = "Request do tworzenia adresu z wymaganymi polami: miasto, ulica, numer domu, numer mieszkania, kod pocztowy oraz dodatkowe informacje.")
public record AddressCreateRequest(

        @Schema(description = "Nazwa miasta", example = "Warszawa")
        @NotBlank
        @Size(max = 50)
        String city,

        @Schema(description = "Nazwa ulicy", example = "Marszałkowska")
        @NotBlank
        @Size(max = 50)
        String street,

        @Schema(description = "Numer ulicy", example = "12")
        @NotBlank
        @Size(max = 10)
        String streetNumber,

        @Schema(description = "Numer mieszkania (opcjonalne)", example = "3A", nullable = true)
        Optional<@Size(max = 10) String> apartmentNumber,

        @Schema(description = "Kod pocztowy", example = "00-001")
        @NotBlank
        @Size(max = 10)
        String postalCode,

        @Schema(description = "Dodatkowe informacje dotyczące adresu", example = "Wjazd od podwórza", nullable = true)
        Optional<@Size(max = 255) String> additionalInformation
) {
}

