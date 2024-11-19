package com.example.amoz.ui.screens.bottom_screens.categories

import com.example.amoz.models.CategoryTree
import java.util.UUID


val testCategoriesList = listOf(
    CategoryTree(
        categoryId = UUID.randomUUID(),
        name = "Electronics",
        categoryLevel = 1,
        childCategories = listOf(
            CategoryTree(
                categoryId = UUID.randomUUID(),
                name = "Smartphones",
                categoryLevel = 2,
                childCategories = listOf(
                    CategoryTree(
                        categoryId = UUID.randomUUID(),
                        name = "Android Phones",
                        categoryLevel = 3,
                        childCategories = listOf()
                    ),
                    CategoryTree(
                        categoryId = UUID.randomUUID(),
                        name = "iPhones",
                        categoryLevel = 3,
                        childCategories = listOf()
                    )
                )
            ),
            CategoryTree(
                categoryId = UUID.randomUUID(),
                name = "Laptops",
                categoryLevel = 2,
                childCategories = listOf(
                    CategoryTree(
                        categoryId = UUID.randomUUID(),
                        name = "Gaming Laptops",
                        categoryLevel = 3,
                        childCategories = listOf()
                    ),
                    CategoryTree(
                        categoryId = UUID.randomUUID(),
                        name = "Business Laptops",
                        categoryLevel = 3,
                        childCategories = listOf()
                    )
                )
            )
        )
    ),
    CategoryTree(
        categoryId = UUID.randomUUID(),
        name = "Home Appliances",
        categoryLevel = 1,
        childCategories = listOf(
            CategoryTree(
                categoryId = UUID.randomUUID(),
                name = "Refrigerators",
                categoryLevel = 2,
                childCategories = listOf()
            ),
            CategoryTree(
                categoryId = UUID.randomUUID(),
                name = "Washing Machines",
                categoryLevel = 2,
                childCategories = listOf()
            )
        )
    ),
    CategoryTree(
        categoryId = UUID.randomUUID(),
        name = "Books",
        categoryLevel = 1,
        childCategories = listOf(
            CategoryTree(
                categoryId = UUID.randomUUID(),
                name = "Fiction",
                categoryLevel = 2,
                childCategories = listOf()
            ),
            CategoryTree(
                categoryId = UUID.randomUUID(),
                name = "Non-fiction",
                categoryLevel = 2,
                childCategories = listOf(
                    CategoryTree(
                        categoryId = UUID.randomUUID(),
                        name = "Biographies",
                        categoryLevel = 3,
                        childCategories = listOf()
                    ),
                    CategoryTree(
                        categoryId = UUID.randomUUID(),
                        name = "Self-help",
                        categoryLevel = 3,
                        childCategories = listOf()
                    )
                )
            )
        )
    )
)

