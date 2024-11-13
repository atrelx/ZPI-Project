package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.ProductVariantDetailsDTO;
import com.zpi.amoz.enums.ImageDirectory;
import com.zpi.amoz.models.ProductVariant;
import com.zpi.amoz.requests.ProductVariantCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.responses.PathResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AuthorizationService;
import com.zpi.amoz.services.FileService;
import com.zpi.amoz.services.ProductVariantService;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @PostMapping
    public ResponseEntity<?> createProductVariant(@Valid @RequestBody ProductVariantCreateRequest request) {
        try {
            ProductVariant productVariant = productVariantService.createProductVariant(request);
            ProductVariantDetailsDTO productVariantDetailsDTO = ProductVariantDetailsDTO.toProductVariantDetailsDTO(productVariant);
            return ResponseEntity.status(HttpStatus.CREATED).body(productVariantDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @PutMapping("/{productVariantId}")
    public ResponseEntity<?> updateProductVariant(@PathVariable UUID productVariantId,
                                                  @Valid @RequestBody ProductVariantCreateRequest request) {
        try {
            ProductVariant productVariant = productVariantService.updateProductVariant(productVariantId, request);
            ProductVariantDetailsDTO productVariantDetailsDTO = ProductVariantDetailsDTO.toProductVariantDetailsDTO(productVariant);
            return ResponseEntity.status(HttpStatus.OK).body(productVariantDetailsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @PatchMapping("/{productVariantId}")
    public ResponseEntity<?> deactivateProductVariant(@PathVariable UUID productVariantId,
                                                      @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);

        if (!authorizationService.hasPermissionToUpdateProductVariant(userPrincipal, productVariantId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product variant is not in your company"));
        }

        try {
            productVariantService.deactivateProductVariantById(productVariantId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getAllProductVariantsByProductId(@PathVariable UUID productId) {
        try {
            List<ProductVariant> productVariantList = productVariantService.findAllByProductId(productId);
            List<ProductVariantDetailsDTO> productVariantDetailsDTOS = productVariantList.stream().map(ProductVariantDetailsDTO::toProductVariantDetailsDTO).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(productVariantDetailsDTOS);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Resource not found: " + e));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An unexpected error occurred: " + e));
        }
    }

    @GetMapping("/{productVariantId}")
    public ResponseEntity<?> getProductVariant(@PathVariable UUID productVariantId) {
        try {
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

    @PutMapping("/picture/{productVariantId}")
    public ResponseEntity<?> uploadProductVariantPicture(
            @PathVariable("productVariantId") UUID productVariantId,
            @RequestParam("file") MultipartFile file) {

        try {
            ProductVariant productVariant = productVariantService.findById(productVariantId)
                    .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found for given ID: " + productVariantId));

            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("No file uploaded."));
            }

            if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("Uploaded file is not an image."));
            }

            BufferedImage bufferedImage = fileService.convertToJpg(file);

            if (bufferedImage == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("Unable to convert file to JPG format."));
            }

            String uploadedFilePath = fileService.saveFile(bufferedImage, ImageDirectory.PRODUCT_VARIANT_IMAGES.getDirectoryName(), productVariantId.toString());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new PathResponse(uploadedFilePath));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to upload file."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Could not found product variant for given ID"));
        }
    }

    @GetMapping("/picture/{productVariantId}")
    public ResponseEntity<byte[]> getProductVariantPicture(@PathVariable("productVariantId") UUID productVariantId) {
        try {
            ProductVariant productVariant = productVariantService.findById(productVariantId)
                    .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found for given ID: " + productVariantId));

            BufferedImage bufferedImage = fileService.downloadFile(ImageDirectory.PRODUCT_VARIANT_IMAGES.getDirectoryName(), productVariantId.toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "JPG", baos);
            byte[] imageBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);

        } catch (IOException | EntityNotFoundException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }



}
