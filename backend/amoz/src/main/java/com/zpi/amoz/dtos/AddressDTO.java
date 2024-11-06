package com.zpi.amoz.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;


public record AddressDTO(
        @NotBlank @Size(max = 50) String city,
        @NotBlank @Size(max = 50) String street,
        @NotBlank @Size(max = 10) String streetNumber,
        @NotBlank @Size(max = 10) String apartmentNumber,
        @NotBlank @Size(max = 10) String postalCode,
        Optional<@Size(max = 255) String> additionalInformation
) {
}