package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.ProductDetailsDTO;
import com.zpi.amoz.dtos.ProductSummaryDTO;
import com.zpi.amoz.models.Product;
import com.zpi.amoz.requests.ProductCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AuthorizationService;
import com.zpi.amoz.services.CompanyService;
import com.zpi.amoz.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AuthorizationService authorizationService;

    @Operation(summary = "Tworzenie nowego produktu", description = "Pozwala na stworzenie nowego produktu w firmie.")
    @ApiResponse(responseCode = "201", description = "Produkt został pomyślnie stworzony",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDetailsDTO.class))
    )
    @ApiResponse(responseCode = "400", description = "Błąd walidacji danych wejściowych",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do tworzenia produktu w tej kategorii lub z tym wariantem",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono firmy dla użytkownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping
    public ResponseEntity<?> createProduct(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody ProductCreateRequest request
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            if (!authorizationService.hasPermissionToManageCategory(userPrincipal, request.categoryId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given category is not in your company"));
            }
            for (UUID productVariantId : request.productVariantIds()) {
                if (!authorizationService.hasPermissionToManageProductVariant(userPrincipal, productVariantId)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given product variant ID is not in your company: " + productVariantId));
                }
            }

            Product createdProduct = productService.createProduct(companyId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProductDetailsDTO.toProductDetailsDTO(createdProduct));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @Operation(summary = "Aktualizowanie produktu", description = "Pozwala na edycję produktu w firmie.")
    @ApiResponse(responseCode = "200", description = "Produkt został pomyślnie zaktualizowany",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDetailsDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do edycji produktu lub kategorii",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu lub firmy",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productId,
            @Valid @RequestBody ProductCreateRequest request
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProduct(userPrincipal, productId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
            }
            if (!authorizationService.hasPermissionToManageCategory(userPrincipal, request.categoryId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given category is not in your company"));
            }
            for (UUID productVariantId : request.productVariantIds()) {
                if (!authorizationService.hasPermissionToManageProductVariant(userPrincipal, productVariantId)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Given product variant ID is not in your company: " + productVariantId));
                }
            }
            Product updatedProduct = productService.updateProduct(productId, request);
            return ResponseEntity.ok(ProductDetailsDTO.toProductDetailsDTO(updatedProduct));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @Operation(summary = "Ustawienie głównego wariantu produktu", description = "Zmienia główny wariant produktu.")
    @ApiResponse(responseCode = "200", description = "Główny wariant produktu został pomyślnie ustawiony",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDetailsDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zmiany wariantu produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu lub wariantu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PatchMapping("/{productId}/mainVariant/{mainVariantId}")
    public ResponseEntity<?> setMainVariant(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productId,
            @PathVariable UUID mainVariantId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProduct(userPrincipal, productId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
            } else if (!authorizationService.hasPermissionToManageProductVariant(userPrincipal, mainVariantId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product variant is not in your company"));
            }
            Product updatedProduct = productService.setMainVariant(productId, mainVariantId);
            return ResponseEntity.ok(ProductDetailsDTO.toProductDetailsDTO(updatedProduct));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @Operation(summary = "Dezaktywacja produktu", description = "Dezaktywuje produkt w systemie.")
    @ApiResponse(responseCode = "204", description = "Produkt został pomyślnie dezaktywowany")
    @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PatchMapping("/{productId}")
    public ResponseEntity<?> deactivateProduct(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProduct(userPrincipal, productId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
            }
            productService.deactivateProductById(productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz wszystkie produkty", description = "Zwraca listę produktów w firmie.")
    @ApiResponse(responseCode = "200", description = "Produkty zostały pomyślnie pobrane",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductSummaryDTO.class)))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal
    ) {
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

    @Operation(summary = "Pobierz szczegóły produktu", description = "Zwraca szczegóły konkretnego produktu.")
    @ApiResponse(responseCode = "200", description = "Produkt został pomyślnie pobrany",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDetailsDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do wyświetlenia produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductDetails(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProduct(userPrincipal, productId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
            }
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
