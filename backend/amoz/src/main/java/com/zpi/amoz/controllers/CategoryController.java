package com.zpi.amoz.controllers;

import com.zpi.amoz.dtos.CategoryDTO;
import com.zpi.amoz.dtos.CategoryTreeDTO;
import com.zpi.amoz.models.Category;
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

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private CompanyService companyService;

    @Operation(summary = "Utwórz kategorię", description = "Tworzy nową kategorię z opcjonalną kategorią nadrzędną.")
    @ApiResponse(responseCode = "201", description = "Kategoria pomyślnie utworzona",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
    )
    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do utworzenia kategorii",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping
    public ResponseEntity<?> createCategory(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                            @Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
        if (categoryCreateRequest.parentCategoryId().isPresent()) {
            if (!authorizationService.hasPermissionToManageCategory(userPrincipal, categoryCreateRequest.parentCategoryId().get())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Parent category is not in your company"));
            }
        }
        try {
            Category category = categoryService.createCategory(userPrincipal.getSub(), categoryCreateRequest);
            CategoryDTO categoryDTO = CategoryDTO.toCategoryDTO(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Zaktualizuj kategorię", description = "Aktualizuje istniejącą kategorię na podstawie jej ID.")
    @ApiResponse(responseCode = "200", description = "Kategoria pomyślnie zaktualizowana",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))
    )
    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do aktualizacji kategorii",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(
            @AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            if (!authorizationService.hasPermissionToManageCategory(userPrincipal, categoryId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Category is not in your company"));
            }
            if (categoryCreateRequest.parentCategoryId().isPresent()) {
                if (!authorizationService.hasPermissionToManageCategory(userPrincipal, categoryCreateRequest.parentCategoryId().get())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Parent category is not in your company"));
                }
            }
            Category category = categoryService.updateCategory(categoryId, categoryCreateRequest);
            CategoryDTO categoryDTO = CategoryDTO.toCategoryDTO(category);
            return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Usuń kategorię", description = "Usuwa istniejącą kategorię na podstawie jej ID.")
    @ApiResponse(responseCode = "204", description = "Kategoria pomyślnie usunięta")
    @ApiResponse(responseCode = "404", description = "Kategoria nie została znaleziona",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Brak uprawnień do usunięcia kategorii",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> authPrincipal,
                                            @PathVariable UUID categoryId) {
        try {
            UserPrincipal userPrincipal = new UserPrincipal(authPrincipal);
            if (!authorizationService.hasPermissionToManageCategory(userPrincipal, categoryId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Category is not in your company"));
            }
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Pobierz wszystkie kategorie firmy", description = "Pobiera wszystkie kategorie dla firmy użytkownika.")
    @ApiResponse(responseCode = "200", description = "Kategorie pomyślnie pobrane",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryTreeDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Nie znaleziono firmy dla podanego ID użytkownika",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
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
