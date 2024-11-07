package com.zpi.amoz.dtos;
import com.zpi.amoz.models.Category;

import java.util.Optional;
import java.util.UUID;

public record CategoryDTO(
        UUID categoryId,
        String name,
        short categoryLevel,
        Optional<CategoryDTO> parentCategory
) {
    public static CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(
                category.getCategoryId(),
                category.getName(),
                category.getCategoryLevel(),
                Optional.ofNullable(category.getParentCategory() != null ? toCategoryDTO(category.getParentCategory()) : null)
        );
    }
}

