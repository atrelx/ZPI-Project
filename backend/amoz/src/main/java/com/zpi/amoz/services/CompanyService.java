package com.zpi.amoz.services;

import com.zpi.amoz.enums.RoleInCompany;
import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.*;
import com.zpi.amoz.requests.CompanyCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findById(UUID id) {
        return companyRepository.findById(id);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Transactional
    public Company createCompany(String userId, CompanyCreateRequest companyDetails) {
        Company company = new Company();
        company.setCompanyNumber(companyDetails.companyNumber());
        company.setCountryOfRegistration(companyDetails.countryOfRegistration());
        company.setName(companyDetails.name());

        companyDetails.regon().ifPresent(company::setRegon);

        Address companyAddress = new Address();
        companyAddress.setCity(companyDetails.address().city());
        companyAddress.setStreet(companyDetails.address().street());
        companyAddress.setStreetNumber(companyDetails.address().streetNumber());
        companyAddress.setApartmentNumber(companyDetails.address().apartmentNumber());
        companyAddress.setPostalCode(companyDetails.address().postalCode());

        companyDetails.address().additionalInformation().ifPresent(companyAddress::setAdditionalInformation);

        Address savedAddress = addressRepository.save(companyAddress);
        company.setAddress(savedAddress);

        Company savedCompany = companyRepository.save(company);

        Employee employee = employeeRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Employee not found with UserId: " + userId));

        employee.setCompany(savedCompany);
        employee.setRoleInCompany(RoleInCompany.OWNER);
        employee.setEmploymentDate(LocalDate.now());
        employeeRepository.save(employee);

        return savedCompany;
    }

    @Transactional
    public Optional<Company> update(UUID id, CompanyCreateRequest companyDetails) {
        return companyRepository.findById(id).map(company -> {
            company.setCompanyNumber(companyDetails.companyNumber());
            company.setCountryOfRegistration(companyDetails.countryOfRegistration());
            company.setName(companyDetails.name());
            companyDetails.regon().ifPresentOrElse(company::setRegon, () -> company.setRegon(null));

            Address companyAddress = company.getAddress();
            companyAddress.setCity(companyDetails.address().city());
            companyAddress.setStreet(companyDetails.address().street());
            companyAddress.setStreetNumber(companyDetails.address().streetNumber());
            companyAddress.setApartmentNumber(companyDetails.address().apartmentNumber());
            companyAddress.setPostalCode(companyDetails.address().postalCode());
            companyDetails.address().additionalInformation()
                    .ifPresentOrElse(companyAddress::setAdditionalInformation, () -> companyAddress.setAdditionalInformation(null));

            addressRepository.save(companyAddress);
            return companyRepository.save(company);
        });
    }

    @Transactional
    public void deactivateCompanyById(UUID id) throws EntityNotFoundException {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find company"));

        for (Employee employee : company.getEmployees()) {
            employee.setCompany(null);
            employee.setRoleInCompany(null);
            employeeRepository.save(employee);
        }

        companyRepository.deactivateCompany(id);
    }
}

