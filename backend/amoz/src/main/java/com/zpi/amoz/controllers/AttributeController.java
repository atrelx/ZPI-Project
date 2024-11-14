package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.AttributeDTO;
import com.zpi.amoz.models.Attribute;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.requests.AttributeCreateRequest;
import com.zpi.amoz.requests.ProductCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AttributeService;
import com.zpi.amoz.services.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attributes")
@RequiredArgsConstructor
public class AttributeController {
    @Autowired
    private AttributeService attributeService;

    @Autowired
    private CompanyService companyService;

    @Operation(summary = "Pobierz wszystkie atrybuty", description = "Pobiera wszystkie atrybuty dostępne dla firmy użytkownika.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano wszystkie atrybuty",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AttributeDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Firma użytkownika nie została znaleziona",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping
    public ResponseEntity<?> getAllAttributes(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            Company company = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("You are not in any company"));
            List<AttributeDTO> attributeDTOs = attributeService.getAllAttributes(company.getCompanyId())
                    .stream().map(AttributeDTO::toAttributeDTO).toList();
            return ResponseEntity.status(HttpStatus.OK).body(attributeDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz atrybuty produktów", description = "Pobiera wszystkie atrybuty związane z produktami dla firmy użytkownika.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano atrybuty produktów",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AttributeDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Firma użytkownika nie została znaleziona",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/product")
    public ResponseEntity<?> getProductAttributes(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            Company company = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("You are not in any company"));
            List<AttributeDTO> attributeDTOs = attributeService.getAllProductAttributes(company.getCompanyId())
                    .stream().map(AttributeDTO::toAttributeDTO).toList();
            return ResponseEntity.status(HttpStatus.OK).body(attributeDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz atrybuty wariantów", description = "Pobiera wszystkie atrybuty związane z wariantami dla firmy użytkownika.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano atrybuty wariantów",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AttributeDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Firma użytkownika nie została znaleziona",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/variant")
    public ResponseEntity<?> getVariantAttributes(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            Company company = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("You are not in any company"));
            List<AttributeDTO> attributeDTOs = attributeService.getAllVariantAttributes(company.getCompanyId())
                    .stream().map(AttributeDTO::toAttributeDTO).toList();
            return ResponseEntity.status(HttpStatus.OK).body(attributeDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }
}


