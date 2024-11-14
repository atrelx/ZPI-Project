package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Address;
import com.zpi.amoz.models.Company;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.zpi.amoz.dtos.AddressDTO.toAddressDTO;

import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące firmę z podstawowymi informacjami o jej działalności.")
public record CompanyDTO(

        @Schema(description = "ID firmy", example = "c4b5fa0f-b8b1-4b60-bb6b-e4b3d91811ef")
        UUID companyId,

        @Schema(description = "Numer identyfikacyjny firmy", example = "123456789")
        String companyNumber,

        @Schema(description = "Nazwa firmy", example = "TechCorp")
        String name,

        @Schema(description = "Kraj rejestracji firmy", example = "Polska")
        String countryOfRegistration,

        @Schema(description = "Adres firmy", example = "{ \"addressId\": \"123e4567-e89b-12d3-a456-426614174000\", \"city\": \"Warszawa\", \"street\": \"Ul. Pięciomorgowa\", \"streetNumber\": \"22\", \"apartmentNumber\": \"5A\", \"postalCode\": \"00-123\", \"additionalInformation\": \"Zamieszkuję na piętrze\" }")
        AddressDTO address,

        @Schema(description = "Numer REGON firmy, opcjonalnie", example = "987654321", nullable = true)
        Optional<String> regon
) {
    public static CompanyDTO toCompanyDTO(Company company) {
        return new CompanyDTO(
                company.getCompanyId(),
                company.getCompanyNumber(),
                company.getCountryOfRegistration(),
                company.getName(),
                AddressDTO.toAddressDTO(company.getAddress()),
                Optional.ofNullable(company.getRegon())
        );
    }
}

