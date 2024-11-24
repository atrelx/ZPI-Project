package com.example.amoz.view_models

import com.example.amoz.api.repositories.CategoryRepository
import com.example.amoz.api.requests.CategoryCreateRequest
import com.example.amoz.models.CategoryTree
import com.example.amoz.ui.states.CategoriesUiState
import com.example.amoz.ui.screens.categories.filtered_list.CategoryListFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): BaseViewModel() {
    private val _categoriesUiState = MutableStateFlow(CategoriesUiState())
    val categoriesUiState: StateFlow<CategoriesUiState> = _categoriesUiState.asStateFlow()
    private val categoryListFilter = CategoryListFilter()

    init {
        fetchCategories()
    }

    fun fetchCategories(skipLoading: Boolean = false) {
        performRepositoryAction(
            binding = _categoriesUiState.value.categoriesListFetched,
            failureMessage = "Could not fetch categories, try again",
            skipLoading = skipLoading,
            action = { categoryRepository.getAllCompanyCategories() },
            onSuccess = { categoriesList ->
                _categoriesUiState.update { it.copy(categoryList = categoriesList) }
                applyFilters()
            }
        )
    }

    fun updateCategory(
        categoryId: UUID,
        categoryCreateRequest: CategoryCreateRequest,
        subcategories: List<String>? = null
        ) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not update category, try later",
            action = { categoryRepository.updateCategory(categoryId, categoryCreateRequest) },
            onSuccess = { parentCategory ->
                subcategories?.let { subcategories ->
                    subcategories.forEach { subcategory ->
                        addCategory(CategoryCreateRequest(subcategory, parentCategory.categoryId))
                    }
                }
                fetchCategories(true)
            }
        )
    }

    fun addCategory(
        categoryCreateRequest: CategoryCreateRequest,
        subcategories: List<String>? = null
    ) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not add category, try later",
            action = { categoryRepository.createCategory(categoryCreateRequest) },
            onSuccess = { parentCategory ->
                subcategories?.let { subcategories ->
                    subcategories.forEach { subcategory ->
                        addCategory(CategoryCreateRequest(subcategory, parentCategory.categoryId))
                    }
                }
                fetchCategories(true)
            }
        )
    }

    fun deleteCategory(categoryId: UUID) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not delete category, try later",
            action = { categoryRepository.deleteCategory(categoryId) },
            onSuccess = { fetchCategories(true) }
        )
    }

    fun updateCurrentCategory(category: CategoryTree?) {
        _categoriesUiState.update { currState ->
            currState.copy(
                currentCategoryTree = category,
                currentCategoryCreateRequest = CategoryCreateRequest(
                    name = category?.name ?: "",
                    parentCategoryId = category?.categoryId?.let {
                        categoryListFilter.findParentId(
                            roots = currState.categoryList,
                            childId = it
                        )
                    }
                )
            )
        }
    }

    private fun applyFilters() {
        _categoriesUiState.update { currState ->
            currState.copy(
                filteredCategoryList = categoryListFilter.filterCategoryList(
                    categoryList = currState.categoryList,
                    searchQuery = currState.searchQuery
                )
            )
        }
    }

    fun updateSearchQuery(searchQuery: String) {
        _categoriesUiState.update { it.copy(searchQuery = searchQuery) }
        applyFilters()
    }

    fun expandAddEditCategoryBottomSheet(expand: Boolean) {
        _categoriesUiState.update { currState ->
            currState.copy(
                addEditCategoryBottomSheetExpanded = expand
            )
        }
    }

    fun expandDeleteCategoryConfirmBottomSheet(expand: Boolean) {
        _categoriesUiState.update { currState ->
            currState.copy(
                deleteCategoryConfirmBottomSheetExpanded = expand
            )
        }
    }
}