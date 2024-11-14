package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.ProductVariantDetailsDTO;
import com.zpi.amoz.enums.ImageDirectory;
import com.zpi.amoz.models.ProductVariant;
import com.zpi.amoz.requests.ProductVariantCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AuthorizationService;
import com.zpi.amoz.services.FileService;
import com.zpi.amoz.services.ProductVariantService;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productVariants")
@RequiredArgsConstructor
public class ProductVariantController {

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private FileService fileService;

    @Operation(summary = "Tworzenie wariantu produktu", description = "Tworzy nowy wariant produktu.")
    @ApiResponse(responseCode = "201", description = "Pomyślnie utworzono wariant produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductVariantDetailsDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zarządzania produktem",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zasobu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping
    public ResponseEntity<?> createProductVariant(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @Valid @RequestBody ProductVariantCreateRequest request
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProduct(userPrincipal, request.productID())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
            }
            ProductVariant productVariant = productVariantService.createProductVariant(request);
            ProductVariantDetailsDTO productVariantDetailsDTO = ProductVariantDetailsDTO.toProductVariantDetailsDTO(productVariant);
            return ResponseEntity.status(HttpStatus.CREATED).body(productVariantDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @Operation(summary = "Aktualizacja wariantu produktu", description = "Aktualizuje dane wariantu produktu.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie zaktualizowano wariant produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductVariantDetailsDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zarządzania produktem lub wariantem",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zasobu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/{productVariantId}")
    public ResponseEntity<?> updateProductVariant(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productVariantId,
            @Valid @RequestBody ProductVariantCreateRequest request
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProduct(userPrincipal, request.productID())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
            } else if (!authorizationService.hasPermissionToManageProductVariant(userPrincipal, productVariantId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product variant is not in your company"));
            }
            ProductVariant productVariant = productVariantService.updateProductVariant(productVariantId, request);
            ProductVariantDetailsDTO productVariantDetailsDTO = ProductVariantDetailsDTO.toProductVariantDetailsDTO(productVariant);
            return ResponseEntity.status(HttpStatus.OK).body(productVariantDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @Operation(summary = "Dezaktywacja wariantu produktu", description = "Dezaktywuje wariant produktu.")
    @ApiResponse(responseCode = "204", description = "Wariant produktu został pomyślnie dezaktywowany")
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zarządzania wariantem produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono wariantu produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PatchMapping("/{productVariantId}")
    public ResponseEntity<?> deactivateProductVariant(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productVariantId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProductVariant(userPrincipal, productVariantId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product variant is not in your company"));
            }
            productVariantService.deactivateProductVariantById(productVariantId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz wszystkie warianty produktu", description = "Pobiera wszystkie warianty produktu na podstawie identyfikatora produktu.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano warianty produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductVariantDetailsDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zarządzania produktem",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zasobu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getAllProductVariantsByProductId(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProduct(userPrincipal, productId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
            }
            List<ProductVariant> productVariantList = productVariantService.findAllByProductId(productId);
            List<ProductVariantDetailsDTO> productVariantDetailsDTOS = productVariantList.stream().map(ProductVariantDetailsDTO::toProductVariantDetailsDTO).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(productVariantDetailsDTOS);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @Operation(summary = "Pobierz szczegóły wariantu produktu", description = "Pobiera szczegóły wariantu produktu na podstawie identyfikatora wariantu.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano szczegóły wariantu produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductVariantDetailsDTO.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zarządzania wariantem produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono wariantu produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/{productVariantId}")
    public ResponseEntity<?> getProductVariant(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID productVariantId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProductVariant(userPrincipal, productVariantId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product variant is not in your company"));
            }
            ProductVariant productVariant = productVariantService.findById(productVariantId)
                    .orElseThrow(() -> new EntityNotFoundException("Could not find product variant for given id: " + productVariantId));
            ProductVariantDetailsDTO productVariantDetailsDTO = ProductVariantDetailsDTO.toProductVariantDetailsDTO(productVariant);
            return ResponseEntity.status(HttpStatus.OK).body(productVariantDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @Operation(summary = "Prześlij zdjęcie wariantu produktu", description = "Przesyła zdjęcie wariantu produktu.")
    @ApiResponse(responseCode = "204", description = "Pomyślnie przesłano zdjęcie")
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zarządzania wariantem produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "Błąd w przesyłaniu pliku",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/picture/{productVariantId}")
    public ResponseEntity<?> uploadProductVariantPicture(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable("productVariantId") UUID productVariantId,
            @RequestParam("file") MultipartFile file
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProductVariant(userPrincipal, productVariantId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product variant is not in your company"));
            }
            fileService.uploadPicutre(file, productVariantId.toString(), ImageDirectory.PRODUCT_VARIANT_IMAGES);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to upload file: " + e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz zdjęcie wariantu produktu", description = "Pobiera zdjęcie wariantu produktu.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano zdjęcie",
            content = @Content(mediaType = "image/jpeg"))
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do zarządzania wariantem produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono zdjęcia wariantu produktu",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/picture/{productVariantId}")
    public ResponseEntity<?> getProductVariantPicture(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable("productVariantId") UUID productVariantId
    ) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            if (!authorizationService.hasPermissionToManageProductVariant(userPrincipal, productVariantId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product variant is not in your company"));
            }

            byte[] imageBytes = fileService.downloadFile(productVariantId.toString(), ImageDirectory.PRODUCT_VARIANT_IMAGES);

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

