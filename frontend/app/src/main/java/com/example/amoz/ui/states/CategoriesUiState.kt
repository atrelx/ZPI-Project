package com.example.amoz.ui.states

import com.example.amoz.models.CategoryTree
import com.example.amoz.ui.screens.bottom_screens.categories.testCategoriesList

data class CategoriesUiState (
    val categoryList: List<CategoryTree> = testCategoriesList,
    val filteredCategoryList: List<CategoryTree> = testCategoriesList,

    val searchQuery: String = "",

    val currAddEditCategory: CategoryTree? = null,
    val addEditCategoryBottomSheetExpanded: Boolean = false,
)