package com.zpi.amoz.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record CompanyCreateRequest(@NotBlank @Size(max = 50) String companyNumber,
                                   @NotBlank @Size(max = 100) String countryOfRegistration,
                                   @NotBlank @Size(max = 100) String name,
                                   @NotNull AddressCreateRequest address,
                                   Optional<@Size(max = 14) String> regon) {
}

