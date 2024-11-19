package com.example.amoz.ui.screens.bottom_screens.categories

import com.example.amoz.data.Category
import com.example.amoz.models.CategoryTree

data class CategoriesUiState (
    val categoryList: List<Category> = testCategoriesList,
    val filteredCategoryList: List<Category> = testCategoriesList,

    val searchQuery: String = "",

    val currAddEditCategory: Category? = null,
    val addEditCategoryBottomSheetExpanded: Boolean = false,
)