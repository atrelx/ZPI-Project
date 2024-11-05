package com.zpi.amoz.services;

import com.zpi.amoz.dtos.CompanyDTO;
import com.zpi.amoz.models.Address;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.repository.AddressRepository;
import com.zpi.amoz.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {

    @Autowired private CompanyRepository companyRepository;

    @Autowired private AddressRepository addressRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findById(UUID id) {
        return companyRepository.findById(id);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> update(UUID id, CompanyDTO companyDetails) {
        return companyRepository.findById(id).map(company -> {
            company.setCompanyNumber(companyDetails.companyNumber());
            company.setCountryOfRegistration(companyDetails.countryOfRegistration());
            company.setName(companyDetails.name());

            if (companyDetails.addressId() != null) {
                UUID addressUuid = UUID.fromString(companyDetails.addressId());
                Address address = addressRepository.findById(addressUuid)
                        .orElseThrow(() -> new EntityNotFoundException("Address not found"));
                company.setAddress(address);
            }

            companyDetails.regon().ifPresent(company::setRegon);
            return companyRepository.save(company);
        });
    }

    public boolean deleteById(UUID id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

