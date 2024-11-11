package com.zpi.amoz.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record CategoryTreeDTO(
    UUID categoryId,
    String name,
    short categoryLevel,
    List<CategoryTreeDTO> childCategories
) {
    public static List<CategoryTreeDTO> buildCategoryTree(List<CategoryDTO> categories) {
        Map<UUID, CategoryTreeDTO> categoryMap = categories.stream()
                .collect(Collectors.toMap(
                        CategoryDTO::categoryId,
                        category -> new CategoryTreeDTO(
                                category.categoryId(),
                                category.name(),
                                category.categoryLevel(),
                                new ArrayList<>()
                        )
                ));

        List<CategoryTreeDTO> rootCategories = new ArrayList<>();

        for (CategoryDTO category : categories) {
            CategoryTreeDTO currentCategory = categoryMap.get(category.categoryId());

            if (category.parentCategory().isPresent()) {
                UUID parentId = category.parentCategory().get().categoryId();
                CategoryTreeDTO parentCategory = categoryMap.get(parentId);
                if (parentCategory != null) {
                    parentCategory.childCategories().add(currentCategory);
                }
            } else {
                rootCategories.add(currentCategory);
            }
        }
        return rootCategories;
    }
}

