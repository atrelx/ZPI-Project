package com.example.amoz.ui.screens.bottom_screens.categories

import com.example.amoz.models.CategoryTree

data class CategoriesUiState (
    val categoryList: List<CategoryTree> = testCategoriesList,
    val filteredCategoryList: List<CategoryTree> = testCategoriesList,

    val searchQuery: String = "",

    val currAddEditCategory: CategoryTree? = null,
    val addEditCategoryBottomSheetExpanded: Boolean = false,
)