package com.zpi.amoz.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record CompanyDTO(
        @NotBlank @Size(max = 50) String companyNumber,
        @NotBlank @Size(max = 100) String countryOfRegistration,
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(min = 36, max = 36) String addressId,
        Optional<@Size(max = 14) String> regon
) {
}