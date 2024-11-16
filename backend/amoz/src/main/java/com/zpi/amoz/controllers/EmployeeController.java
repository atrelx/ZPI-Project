package com.zpi.amoz.controllers;

import com.azure.core.annotation.Delete;
import com.zpi.amoz.dtos.EmployeeDTO;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.User;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AuthorizationService;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.EmployeeService;
import com.zpi.amoz.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AuthorizationService authorizationService;

    @Operation(summary = "Zaakceptuj zaproszenie do firmy", description = "Umożliwia pracownikowi zaakceptowanie zaproszenia do firmy.")
    @ApiResponse(responseCode = "200", description = "Zaproszenie zostało zaakceptowane pomyślnie")
    @ApiResponse(responseCode = "400", description = "Błąd w przetwarzaniu zaproszenia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping("/acceptInvitation")
    public ResponseEntity<Void> acceptInvitationToCompany(
            @RequestParam String token
    ) {
        UUID confirmationToken = UUID.fromString(token);
        employeeService.acceptInvitationToCompany(confirmationToken);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Zaproś pracownika do firmy", description = "Wysyła zaproszenie do pracownika do dołączenia do firmy.")
    @ApiResponse(responseCode = "204", description = "Zaproszenie zostało wysłane pomyślnie")
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zapraszania pracownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono firmy dla użytkownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping("/invite")
    public ResponseEntity<?> inviteEmployeeToCompany(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @RequestParam String employeeEmail
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            if (!authorizationService.hasPermissionToManageCompany(userPrincipal, companyId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("You are not company owner"));
            }
            employeeService.inviteEmployeeToCompany(companyId, employeeEmail).get();
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Usuń pracownika z firmy", description = "Usuwa pracownika z firmy na podstawie jego identyfikatora.")
    @ApiResponse(responseCode = "204", description = "Pracownik został pomyślnie usunięty z firmy")
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do usuwania pracownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono pracownika lub firmy",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PatchMapping("/kick/{employeeId}")
    public ResponseEntity<?> kickEmployeeFromCompany(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID employeeId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            if (!authorizationService.hasPermissionToManageCompany(userPrincipal, companyId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("You are not company owner"));
            }
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

    @Operation(summary = "Pracownik opuszcza firmę", description = "Pracownik opuszcza firmę, do której należy.")
    @ApiResponse(responseCode = "204", description = "Pracownik pomyślnie opuścił firmę")
    @ApiResponse(responseCode = "404", description = "Nie znaleziono pracownika lub firmy",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PatchMapping("/leave")
    public ResponseEntity<?> leaveCompany(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {

            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("You are not in any company"))
                    .getCompanyId();
            UUID employeeId = employeeService.findEmployeeByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("You are not in any company"))
                    .getEmployeeId();
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

    @Operation(summary = "Pobierz listę pracowników", description = "Zwraca listę wszystkich pracowników przypisanych do firmy.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano listę pracowników",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class)))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono pracowników",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping
    public ResponseEntity<?> fetchEmployees(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            List<Employee> employees = employeeService.getEmployeesByCompanyId(companyId);
            List<EmployeeDTO> employeeDTOs = employees.stream().map(EmployeeDTO::toEmployeeDTO).collect(Collectors.toList());
            return ResponseEntity.ok(employeeDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }
}
