package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.CategoryDTO;
import com.zpi.amoz.dtos.CategoryTreeDTO;
import com.zpi.amoz.models.Category;
import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.Product;
import com.zpi.amoz.requests.CategoryCreateRequest;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.security.UserPrincipal;
import com.zpi.amoz.services.AuthorizationService;
import com.zpi.amoz.services.CategoryService;
import com.zpi.amoz.services.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired private CategoryService categoryService;

    @Autowired private AuthorizationService authorizationService;

    @Autowired private CompanyService companyService;

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        try {
            Category createdCategory = categoryService.createCategory(categoryCreateRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(CategoryDTO.toCategoryDTO(createdCategory));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Update Category", description = "Update an existing category by its ID")
    @ApiResponse(responseCode = "200", description = "Category successfully updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
    )
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Unauthorized to update category",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            if (!authorizationService.hasPermissionToUpdateCategory(userPrincipal, categoryId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
            }
            Category updatedCategory = categoryService.updateCategory(categoryId, categoryCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(CategoryDTO.toCategoryDTO(updatedCategory));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID categoryId) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            if (!authorizationService.hasPermissionToUpdateCategory(userPrincipal, categoryId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Product is not in your company"));
            }
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCompanyCategories(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        try {
            UUID companyId = companyService.getCompanyByUserId(userPrincipal.getSub())
                    .orElseThrow(() -> new EntityNotFoundException("Could not found company for given user ID"))
                    .getCompanyId();
            List<CategoryTreeDTO> categoryTree = categoryService.getCategoryTree(companyId);
            return ResponseEntity.status(HttpStatus.OK).body(categoryTree);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }
}
