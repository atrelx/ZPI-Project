package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;
import java.util.UUID;

@Schema(description = "Podsumowanie kategorii produktu")
public record CategorySummaryDTO(

        @Schema(description = "ID kategorii", example = "e4b5fa0f-b8b1-4b60-bb6b-e4b3d91811ed")
        UUID categoryId,

        @Schema(description = "Nazwa kategorii", example = "Elektronika")
        String name,

        @Schema(description = "Poziom kategorii w hierarchii", example = "1")
        short categoryLevel
) {

    public static CategorySummaryDTO toCategorySummaryDTO(Category category) {
        return new CategorySummaryDTO(
                category.getCategoryId(),
                category.getName(),
                category.getCategoryLevel()
        );
    }
}


