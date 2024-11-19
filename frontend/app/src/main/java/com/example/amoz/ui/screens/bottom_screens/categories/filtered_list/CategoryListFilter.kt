package com.example.amoz.ui.screens.bottom_screens.categories.filtered_list

import com.example.amoz.data.Category

class CategoryListFilter {
    fun filterCategoryList(
        categoryList: List<Category>,
        searchQuery: String
    ): List<Category> {
        if (searchQuery.isBlank()) {
            return categoryList.filter { it.categoryLevel.toInt() == 1 }
        }

        val filteredList = categoryList.filter { category ->
            category.name.contains(searchQuery, ignoreCase = true)
        }

        val filteredChildren = categoryList.flatMap { category ->
            filterCategoryList(category.childCategories, searchQuery)
        }

        return filteredList + filteredChildren
    }



}