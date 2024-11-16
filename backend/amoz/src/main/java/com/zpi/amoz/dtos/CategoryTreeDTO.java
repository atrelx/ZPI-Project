package com.zpi.amoz.dtos;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Schema(description = "Reprezentacja kategorii w drzewie kategorii z podkategoriami")
public record CategoryTreeDTO(

        @Schema(description = "ID kategorii", example = "e4b5fa0f-b8b1-4b60-bb6b-e4b3d91811ed")
        UUID categoryId,

        @Schema(description = "Nazwa kategorii", example = "Elektronika")
        String name,

        @Schema(description = "Poziom kategorii w hierarchii", example = "1")
        short categoryLevel,

        @ArraySchema(schema = @Schema(description = "Lista podkategorii", implementation = CategoryTreeDTO.class))
        List<CategoryTreeDTO> childCategories
) {

    public static List<CategoryTreeDTO> buildCategoryTree(List<CategoryDetailsDTO> categories) {
        Map<UUID, CategoryTreeDTO> categoryMap = categories.stream()
                .collect(Collectors.toMap(
                        CategoryDetailsDTO::categoryId,
                        category -> new CategoryTreeDTO(
                                category.categoryId(),
                                category.name(),
                                category.categoryLevel(),
                                new ArrayList<>()
                        )
                ));

        List<CategoryTreeDTO> rootCategories = new ArrayList<>();

        for (CategoryDetailsDTO category : categories) {
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


