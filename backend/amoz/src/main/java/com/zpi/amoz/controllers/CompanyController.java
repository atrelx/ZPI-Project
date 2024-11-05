package com.zpi.amoz.controllers;


import com.zpi.amoz.dtos.CompanyDTO;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;


//    @PostMapping
//    public ResponseEntity<Company> createCompany(@RequestBody CompanyDTO companyDetails) {
//        Company createdCompany = c
//                //Create based on ID;
//        Company createdCompany = companyService.save(company);
//        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable UUID id) {
        Optional<Company> company = companyService.findById(id);
        return company.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable UUID id, @RequestBody CompanyDTO companyDetails) {
        Optional<Company> updatedCompany = companyService.update(id, companyDetails);
        return updatedCompany.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        boolean isDeleted = companyService.deleteById(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
