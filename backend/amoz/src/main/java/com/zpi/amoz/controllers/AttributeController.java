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
}
