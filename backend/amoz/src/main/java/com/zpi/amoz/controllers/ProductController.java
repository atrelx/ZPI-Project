package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.ProductDetailsDTO;
import com.zpi.amoz.dtos.ProductSummaryDTO;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.Product;
import com.zpi.amoz.requests.ProductCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AuthorizationService;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping
    public ResponseEntity<?> createProduct(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                           @Valid @RequestBody ProductCreateRequest request) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                .getCompanyId();
        try {
            Product createdProduct = productService.createProduct(companyId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProductDetailsDTO.toProductDetailsDTO(createdProduct));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID productId,
                                           @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                           @Valid @RequestBody ProductCreateRequest request) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);

        if (!authorizationService.hasPermissionToUpdateProduct(userPrincipal, productId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
        }

        try {
            Product updatedProduct = productService.updateProduct(productId, request);
            return ResponseEntity.ok(ProductDetailsDTO.toProductDetailsDTO(updatedProduct));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> deactivateProduct(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                               @PathVariable UUID productId) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);

        if (!authorizationService.hasPermissionToUpdateProduct(userPrincipal, productId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
        }

        try {
            productService.deactivateProductById(productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();

            List<Product> products = productService.findAllByCompanyId(companyId);
            List<ProductSummaryDTO> productSummaryDTOs = products.stream()
                    .map(ProductSummaryDTO::toProductSummaryDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(productSummaryDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while fetching products: " + e.getMessage()));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductDetails(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productId) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);

        if (!authorizationService.hasPermissionToUpdateProduct(userPrincipal, productId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
        }

        try {
            Product product = productService.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

            ProductDetailsDTO productDetailsDTO = ProductDetailsDTO.toProductDetailsDTO(product);

            return ResponseEntity.ok(productDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while fetching product details: " + e.getMessage()));
        }
    }
}
