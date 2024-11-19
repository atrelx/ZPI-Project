package com.example.amoz.ui.screens.bottom_screens.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.amoz.data.Category
import com.example.amoz.ui.screens.bottom_screens.categories.filtered_list.CategoryListFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class CategoriesViewModel: ViewModel() {
    private val _categoriesUiState = MutableStateFlow(CategoriesUiState())
    val categoriesUiState: StateFlow<CategoriesUiState> = _categoriesUiState.asStateFlow()
    val categoryListFilter = CategoryListFilter()

    fun updateCategory(categoryId: UUID, newName: String) {
        /*TODO*/

    }

    fun addCategory(categoryName: String, parentCategoryId: UUID? = null) {
        //TODO
    }

    fun addSubcategoriesToCategory(subcategoriesList: List<String>, parentCategoryId: UUID? = null) {
        subcategoriesList.forEach {
            addCategory(it, parentCategoryId)
        }
    }

    fun updateCurrentAddEditCategory(category: Category?) {
        _categoriesUiState.update { currState ->
            currState.copy(
                currAddEditCategory = category
            )
        }
    }

    fun updateSearchQuery(searchQuery: String) {
        _categoriesUiState.update { currState ->
            val filteredList =
                if (currState.searchQuery.isNotBlank())
                categoryListFilter.filterCategoryList(
                    categoryList = currState.categoryList,
                    searchQuery = searchQuery
                )
                else currState.categoryList

            currState.copy(
                searchQuery = searchQuery,
                filteredCategoryList = categoryListFilter.filterCategoryList(
                    categoryList = filteredList,
                    searchQuery = searchQuery
                )
            )
        }
    }

    fun expandAddEditCategoryBottomSheet(expand: Boolean) {
        _categoriesUiState.update { currState ->
            currState.copy(
                addEditCategoryBottomSheetExpanded = expand
            )
        }
    }
}