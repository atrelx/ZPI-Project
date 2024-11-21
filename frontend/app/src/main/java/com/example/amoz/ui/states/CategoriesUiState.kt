package com.example.amoz.ui.states

import com.example.amoz.models.CategoryTree
import com.example.amoz.test_data.categories.testCategoriesList

data class CategoriesUiState (
    val categoryList: List<CategoryTree> = testCategoriesList,
    val filteredCategoryList: List<CategoryTree> = testCategoriesList,

    val searchQuery: String = "",

    val currAddEditCategory: CategoryTree? = null,
    val addEditCategoryBottomSheetExpanded: Boolean = false,
)