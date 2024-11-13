package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.ProductOrderDetailsDTO;
import com.zpi.amoz.dtos.ProductOrderSummaryDTO;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.ProductOrder;
import com.zpi.amoz.requests.ProductOrderCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AuthorizationService;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.ProductOrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productOrders")
@RequiredArgsConstructor
public class ProductOrderController {
    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<?> createProductOrder(@Valid @RequestBody ProductOrderCreateRequest request) {
        try {
            ProductOrder productOrder = productOrderService.createProductOrder(request);
            ProductOrderDetailsDTO productOrderDetailsDTO = ProductOrderDetailsDTO.toProductOrderDetailsDTO(productOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(productOrderDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @PutMapping("/{productOrderId}")
    public ResponseEntity<?> updateProductOrder(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                                @PathVariable UUID productOrderId,
                                                @Valid @RequestBody ProductOrderCreateRequest request) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);

        if (!authorizationService.hasPermissionToUpdateProductOrder(userPrincipal, productOrderId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product order is not in your company"));
        }

        try {
            ProductOrder productOrder = productOrderService.updateProductOrder(productOrderId, request);
            ProductOrderDetailsDTO productOrderDetailsDTO = ProductOrderDetailsDTO.toProductOrderDetailsDTO(productOrder);
            return ResponseEntity.status(HttpStatus.OK).body(productOrderDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @GetMapping("/{productOrderId}")
    public ResponseEntity<?> getProductOrderDetails(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productOrderId) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);

        if (!authorizationService.hasPermissionToUpdateProductOrder(userPrincipal, productOrderId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product order is not in your company"));
        }

        try {
            ProductOrder productOrder = productOrderService.findById(productOrderId)
                    .orElseThrow(() -> new EntityNotFoundException("Product order not found with id: " + productOrderId));

            ProductOrderDetailsDTO productOrderDetailsDTO = ProductOrderDetailsDTO.toProductOrderDetailsDTO(productOrder);

            return ResponseEntity.ok(productOrderDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while fetching product details: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProductOrderSummary(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("You are not in any company"))
                    .getCompanyId();
            List<ProductOrder> productOrders = productOrderService.findByCompanyId(companyId);

            List<ProductOrderSummaryDTO> productOrderSummaryDTOs = productOrders.stream()
                    .map(ProductOrderSummaryDTO::toProductOrderSummaryDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(productOrderSummaryDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while fetching product details: " + e.getMessage()));
        }
    }
}
