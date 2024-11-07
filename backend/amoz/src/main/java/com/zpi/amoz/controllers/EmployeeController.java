package com.zpi.amoz.controllers;

import com.azure.core.annotation.Delete;
import com.zpi.amoz.dtos.EmployeeDTO;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.User;
import com.zpi.amoz.responses.EmployeeDetailsResponse;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.EmployeeService;
import com.zpi.amoz.services.UserService;
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
    private UserService userService;

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
        User user = userService.findById(userPrincipal.getSub())
                .orElseThrow(() -> new EntityNotFoundException("You are not registered"));

        Company company = Optional.ofNullable(user.getEmployee().getCompany())
                .orElseThrow(() -> new EntityNotFoundException("You are not in any company"));

        try {
            employeeService.inviteEmployeeToCompany(company.getCompanyId(), employeeEmail).get();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }

    @PatchMapping("/{companyId}/kick/{employeeId}")
    public ResponseEntity<MessageResponse> kickEmployeeFromCompany(@PathVariable UUID companyId,
                                                                   @PathVariable UUID employeeId) {
        try {
            employeeService.kickFromCompanyById(companyId, employeeId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByCompanyId(@PathVariable UUID companyId) {
        try {
            List<EmployeeDTO> employees = employeeService.getEmployeesByCompanyId(companyId);
            return ResponseEntity.ok(employees);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
