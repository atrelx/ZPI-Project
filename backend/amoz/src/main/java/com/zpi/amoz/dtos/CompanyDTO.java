package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Address;
import com.zpi.amoz.models.Company;

import static com.zpi.amoz.dtos.AddressDTO.toAddressDTO;

import java.util.Optional;
import java.util.UUID;

public record CompanyDTO(UUID companyId,
                         String companyNumber,
                         String countryOfRegistration,
                         String name,
                         AddressDTO address,
                         Optional<String> regon) {
    static CompanyDTO toCompanyDTO(Company company) {
        return new CompanyDTO(
                company.getCompanyId(),
                company.getCompanyNumber(),
                company.getCountryOfRegistration(),
                company.getName(),
                toAddressDTO(company.getAddress()),
                Optional.ofNullable(company.getRegon())
        );
    }
}