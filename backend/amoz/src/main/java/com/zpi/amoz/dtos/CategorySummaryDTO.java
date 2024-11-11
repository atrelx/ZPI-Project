package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Category;

import java.util.Optional;
import java.util.UUID;

public record CategorySummaryDTO(
        UUID categoryId,
        String name,
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

