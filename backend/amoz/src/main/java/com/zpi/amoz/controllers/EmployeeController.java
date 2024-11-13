package com.zpi.amoz.controllers;

import com.azure.core.annotation.Delete;
import com.zpi.amoz.dtos.EmployeeDTO;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.User;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.EmployeeService;
import com.zpi.amoz.services.UserService;
import jakarta.mail.Message;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompanyService companyService;

    @PostMapping("/acceptInvitation")
    public ResponseEntity<Void> acceptInvitationToCompany(@RequestParam String token) {
        UUID confirmationToken = UUID.fromString(token);
        employeeService.acceptInvitationToCompany(confirmationToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/invite")
    public ResponseEntity<MessageResponse> inviteEmployeeToCompany(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                                                   @RequestParam String employeeEmail) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                .getCompanyId();
        try {
            employeeService.inviteEmployeeToCompany(companyId, employeeEmail).get();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }

    @PatchMapping("/kick/{employeeId}")
    public ResponseEntity<MessageResponse> kickEmployeeFromCompany(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID employeeId) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            employeeService.kickFromCompanyById(companyId, employeeId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByCompanyId(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            List<EmployeeDTO> employees = employeeService.getEmployeesByCompanyId(companyId);
            return ResponseEntity.ok(employees);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
