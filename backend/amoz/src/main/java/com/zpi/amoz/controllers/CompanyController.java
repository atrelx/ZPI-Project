package com.zpi.amoz.controllers;


import com.zpi.amoz.dtos.CompanyDTO;
import com.zpi.amoz.models.Address;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<Company> createCompany(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                                 @Valid @RequestBody CompanyDTO companyDetails) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        Company company = companyService.createCompany(userPrincipal.getSub(), companyDetails);
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable UUID id) {
        return companyService.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable UUID id,
                                                 @Valid @RequestBody CompanyDTO companyDetails) {
        return companyService.update(id, companyDetails)
                .map(updatedCompany -> new ResponseEntity<>(updatedCompany, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> deactivateCompany(@PathVariable UUID id) {
        boolean isDeactivated = companyService.deactivateCompanyById(id);
        return isDeactivated ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/acceptInvitation")
    public ResponseEntity<Void> acceptInvitationToCompany(@RequestParam String token) {
        UUID confirmationToken = UUID.fromString(token);
        companyService.acceptInvitationToCompany(confirmationToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{companyId}/invite")
    public ResponseEntity<String> inviteEmployeeToCompany(@PathVariable UUID companyId,
                                                        @RequestParam String employeeEmail) {
        try {
            boolean emailSent = companyService.inviteEmployeeToCompany(companyId, employeeEmail).get();

            if (emailSent) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
