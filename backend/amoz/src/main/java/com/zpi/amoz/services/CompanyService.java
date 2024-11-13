package com.zpi.amoz.services;

import com.zpi.amoz.enums.RoleInCompany;
import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.*;
import com.zpi.amoz.requests.CompanyCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findById(UUID id) {
        return companyRepository.findById(id);
    }

    public Optional<Company> getCompanyByUserId(String userId) {
        return companyRepository.getCompanyByUserId(userId);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Transactional
    public Company createCompany(String userId, CompanyCreateRequest request) {
        Company company = new Company();
        company.setCompanyNumber(request.companyNumber());
        company.setCountryOfRegistration(request.countryOfRegistration());
        company.setName(request.name());

        request.regon().ifPresent(company::setRegon);

        Address companyAddress = addressService.createAddress(request.address());
        company.setAddress(companyAddress);

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
    public Optional<Company> updateCompany(UUID id, CompanyCreateRequest request) {
        return companyRepository.findById(id).map(company -> {
            company.setCompanyNumber(request.companyNumber());
            company.setCountryOfRegistration(request.countryOfRegistration());
            company.setName(request.name());
            request.regon().ifPresentOrElse(company::setRegon, () -> company.setRegon(null));

            addressService.updateAddress(company.getAddress().getAddressId(), request.address());

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

