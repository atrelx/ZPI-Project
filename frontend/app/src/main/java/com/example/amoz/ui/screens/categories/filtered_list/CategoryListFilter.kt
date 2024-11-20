package com.example.amoz.ui.screens.categories.filtered_list

import com.example.amoz.models.CategoryTree

class CategoryListFilter {
    fun filterCategoryList(
        categoryList: List<CategoryTree>,
        searchQuery: String
    ): List<CategoryTree> {
        if (searchQuery.isBlank()) {
            return categoryList.filter { it.categoryLevel.toInt() == 1 }
        }

        val resultSet = mutableSetOf<CategoryTree>()

        val filteredList = categoryList.filter { category ->
            category.name.contains(searchQuery, ignoreCase = true)
        }
        resultSet.addAll(filteredList)

        val filteredChildren = categoryList.flatMap { category ->
            filterCategoryList(category.childCategories, searchQuery)
        }
        resultSet.addAll(filteredChildren)

        return resultSet.toList()
    }
}
