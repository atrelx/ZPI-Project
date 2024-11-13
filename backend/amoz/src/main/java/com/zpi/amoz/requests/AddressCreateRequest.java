package com.zpi.amoz.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record AddressCreateRequest(
        @NotBlank @Size(max = 50) String city,
        @NotBlank @Size(max = 50) String street,
        @NotBlank @Size(max = 10) String streetNumber,
        @NotBlank @Size(max = 10) String apartmentNumber,
        @NotBlank @Size(max = 10) String postalCode,
        Optional<@Size(max = 255) String> additionalInformation
) {
}
