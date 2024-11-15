package com.example.amoz.ui.screens.bottom_screens.categories

import com.example.amoz.data.Category

data class CategoriesUiState (
    val categoryList: List<Category> = testCategoriesList,
    val searchQuery: String = "",
    val addEditCategoryBottomSheetExpanded: Boolean = false,
)