package com.zpi.amoz.controllers;

import com.azure.core.annotation.Delete;
import com.zpi.amoz.dtos.EmployeeDTO;
import com.zpi.amoz.dtos.InvitationDTO;
import com.zpi.amoz.enums.ImageDirectory;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.Employee;
import com.zpi.amoz.models.Invitation;
import com.zpi.amoz.models.User;
import com.zpi.amoz.requests.PushRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.*;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @Autowired
    private FileService fileService;

    @Operation(summary = "Zaakceptuj zaproszenie do firmy", description = "Umożliwia pracownikowi zaakceptowanie zaproszenia do firmy.")
    @ApiResponse(responseCode = "204", description = "Zaproszenie zostało zaakceptowane pomyślnie")
    @ApiResponse(responseCode = "400", description = "Błąd w przetwarzaniu zaproszenia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping("/acceptInvitation")
    public ResponseEntity<Void> acceptInvitationToCompany(
            @RequestParam String token
    ) {
        UUID confirmationToken = UUID.fromString(token);
        employeeService.acceptInvitationToCompany(confirmationToken);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Odrzuć zaproszenie do firmy", description = "Umożliwia pracownikowi odrzucenie zaproszenia do firmy.")
    @ApiResponse(responseCode = "200", description = "Zaproszenie zostało odrzucone pomyślnie")
    @ApiResponse(responseCode = "400", description = "Błąd w przetwarzaniu zaproszenia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping("/rejectInvitation")
    public ResponseEntity<Void> rejectInvitationToCompany(
            @RequestParam String token
    ) {
        UUID confirmationToken = UUID.fromString(token);
        employeeService.rejectInvitationToCompany(confirmationToken);
        return ResponseEntity.noContent().build();
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

    @Operation(summary = "Pobieranie zdjęcia profilowego", description = "Pobiera zdjęcie profilowe pracownika.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano zdjęcie",
            content = @Content(mediaType = "image/jpeg"))
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zdjęcia",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/picture/{employeeId}")
    public ResponseEntity<?> getEmployeeProfilePicture(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID employeeId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID employeeCompanyId = companyService.getCompanyByEmployeeId(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();

            if (!authorizationService.hasPermissionToReadCompany(userPrincipal, employeeCompanyId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("You are not company worker"));
            }

            String employeeUserId = employeeService.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Could not found employee for given employee ID"))
                    .getUser()
                    .getUserId();

            byte[] imageBytes = fileService.downloadFile(employeeUserId, ImageDirectory.USER_IMAGES);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
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
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
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

    @Operation(summary = "Pobierz dane pracownika", description = "Zwraca dane pracownika.")
    @ApiResponse(responseCode = "200",
            description = "Pomyślnie pobrano dane pracownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class)))
    @ApiResponse(responseCode = "404",
            description = "Nie znaleziono pracownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    @ApiResponse(responseCode = "500",
            description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    @GetMapping("/employee")
    public ResponseEntity<?> fetchEmployeeByUserId(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            Employee employee = employeeService.getEmployeeByUserId(userPrincipal.getSub());
            EmployeeDTO employeeDTO = EmployeeDTO.toEmployeeDTO(employee);
            return ResponseEntity.ok(employeeDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz zaproszenia", description = "Zwraca listę wszystkich zaproszeń użytkownika do firm.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano zaproszenia",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = InvitationDTO.class)))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono pracowników",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/invitations")
    public ResponseEntity<?> fetchInvitations(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            List<Invitation> invitations = employeeService.fetchAllInvitations(userPrincipal.getSub());
            List<InvitationDTO> invitationDTOs = invitations.stream().map(InvitationDTO::toInvitationDTO).collect(Collectors.toList());
            return ResponseEntity.ok(invitationDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }
}
