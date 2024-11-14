package com.zpi.amoz.controllers;


import com.zpi.amoz.dtos.CompanyDTO;
import com.zpi.amoz.enums.ImageDirectory;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.requests.CompanyCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AuthorizationService;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private FileService fileService;

    @Autowired
    private AuthorizationService authorizationService;

    @Operation(summary = "Utwórz firmę", description = "Tworzy nową firmę na podstawie danych przekazanych w żądaniu.")
    @ApiResponse(responseCode = "201", description = "Firma została pomyślnie utworzona",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyDTO.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping
    public ResponseEntity<?> createCompany(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody CompanyCreateRequest request
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            Company company = companyService.createCompany(userPrincipal.getSub(), request);
            return ResponseEntity.status(HttpStatus.CREATED).body(CompanyDTO.toCompanyDTO(company));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected exception has occured: " + e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz firmę użytkownika", description = "Zwraca firmę powiązaną z danym użytkownikiem.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano firmę",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono firmy dla użytkownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping
    public ResponseEntity<?> getUserCompany(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            Company company = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"));

            return ResponseEntity.status(HttpStatus.OK).body(CompanyDTO.toCompanyDTO(company));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Aktualizuj firmę", description = "Aktualizuje dane firmy na podstawie przekazanych danych.")
    @ApiResponse(responseCode = "200", description = "Firma została pomyślnie zaktualizowana",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zarządzania firmą",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Firma nie została znaleziona",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping
    public ResponseEntity<?> updateCompany(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody CompanyCreateRequest request
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID: " + userPrincipal.getSub()))
                    .getCompanyId();
            if (!authorizationService.hasPermissionToManageCompany(userPrincipal, companyId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("You are not company owner"));
            }
            Company updatedCompany = companyService.updateCompany(userPrincipal.getSub(), request);
            return ResponseEntity.status(HttpStatus.OK).body(CompanyDTO.toCompanyDTO(updatedCompany));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Unexpected exception occured: " + e.getMessage()));
        }
    }

    @Operation(summary = "Dezaktywuj firmę", description = "Dezaktywuje firmę powiązaną z danym użytkownikiem.")
    @ApiResponse(responseCode = "204", description = "Firma została pomyślnie dezaktywowana")
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do dezaktywacji firmy",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Firma nie została znaleziona",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PatchMapping
    public ResponseEntity<MessageResponse> deactivateCompany(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            if (!authorizationService.hasPermissionToManageCompany(userPrincipal, companyId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("You are not company owner"));
            }
            companyService.deactivateCompanyById(companyId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Wgraj zdjęcie profilowe firmy", description = "Wgrywa zdjęcie profilowe firmy.")
    @ApiResponse(responseCode = "204", description = "Zdjęcie profilowe firmy zostało pomyślnie wgrane")
    @ApiResponse(responseCode = "400", description = "Błąd wczytywania pliku",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zmiany zdjęcia firmy",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Firma nie została znaleziona",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/picture")
    public ResponseEntity<?> uploadCompanyProfilePicture(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @RequestParam("file") MultipartFile file
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            if (!authorizationService.hasPermissionToManageCompany(userPrincipal, companyId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("You are not company owner"));
            }
            fileService.uploadPicutre(file, companyId.toString(), ImageDirectory.COMPANY_IMAGES);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to upload file." + e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz zdjęcie profilowe firmy", description = "Pobiera zdjęcie profilowe firmy.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano zdjęcie profilowe",
            content = @Content(mediaType = "image/jpeg"))
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zdjęcia firmy",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/picture")
    public ResponseEntity<?> getCompanyProfilePicture(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();

            byte[] imageBytes = fileService.downloadFile(companyId.toString(), ImageDirectory.COMPANY_IMAGES);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }
}
