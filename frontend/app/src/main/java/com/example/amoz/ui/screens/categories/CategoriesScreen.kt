package com.example.amoz.ui.screens.categories

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.models.CategoryTree
import com.example.amoz.ui.components.bottom_sheets.ConfirmDeleteItemBottomSheet
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.screens.categories.filtered_list.CategoriesFilteredList
import com.example.amoz.view_models.CategoriesViewModel
import kotlinx.serialization.json.Json

@Composable
fun CategoriesScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    categoryViewModel: CategoriesViewModel = hiltViewModel()
) {
    val categoryUiState by categoryViewModel.categoriesUiState.collectAsState()

    var isSelectableLeavesOnly = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Boolean>("isSelectableLeavesOnly") ?: false

    var isSelectable = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Boolean>("isSelectable") ?: false

    if(isSelectableLeavesOnly) {isSelectable = true}

    fun closeAddEditCategoryBottomSheet() {
        categoryViewModel.updateCurrentCategory(null)
        categoryViewModel.expandAddEditCategoryBottomSheet(false)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        ResultStateView(
            state = categoryUiState.categoriesListFetched,
            onPullToRefresh = { categoryViewModel.fetchCategories(true) }
        ) {
            CategoriesFilteredList(
                categories = categoryUiState.filteredCategoryList,
                searchQuery = categoryUiState.searchQuery,
                isSelectable = isSelectable,
                isSelectableLeavesOnly = isSelectableLeavesOnly,
                onSearchQueryChange = categoryViewModel::updateSearchQuery,
                onClick = {
                    if (isSelectable) {
                        val categoryJson = Json.encodeToString(CategoryTree.serializer(), it)
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "selectedCategoryTree", categoryJson
                        )
                        navController.popBackStack()
                        isSelectable = false
                        isSelectableLeavesOnly = false
                    }
                    else {
                        categoryViewModel.updateCurrentCategory(it)
                        categoryViewModel.expandAddEditCategoryBottomSheet(true)
                    }
                },
                onAdd = {
                    categoryViewModel.updateCurrentCategory(null)
                    categoryViewModel.expandAddEditCategoryBottomSheet(true)
                },
                onDelete = {
                    categoryViewModel.updateCurrentCategory(it)
                    categoryViewModel.expandDeleteCategoryConfirmBottomSheet(true)
                }
            )
        }

        // -------------------- Bottom sheet title --------------------
        Box(modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    categoryViewModel.updateCurrentCategory(null)
                    categoryViewModel.expandAddEditCategoryBottomSheet(true)
                },
                modifier = Modifier
                    .padding(16.dp),
                icon = { Icon(Icons.Filled.Add, null) },
                text = { Text(text = stringResource(id = R.string.add_category_title)) }
            )
        }
    }

    if (categoryUiState.addEditCategoryBottomSheetExpanded) {
        categoryUiState.currentCategoryCreateRequest?.let {
            AddEditCategoryBottomSheet(
                categoryTree = categoryUiState.currentCategoryTree,
                categoryCreateRequest = it,
                onDismissRequest = { closeAddEditCategoryBottomSheet() },
                onComplete = { name, subcategoriesList ->
                    if (categoryUiState.currentCategoryTree != null) {
                        val categoryId = categoryUiState.currentCategoryTree!!.categoryId
                        categoryViewModel.updateCategory(categoryId, name, subcategoriesList)
                    }
                    else {
                        categoryViewModel.addCategory(name, subcategoriesList)
                    }
                },
                onSubcategoryEdit = categoryViewModel::updateCurrentCategory
            )
        }
    }

    if(categoryUiState.deleteCategoryConfirmBottomSheetExpanded) {
        categoryUiState.currentCategoryTree?.let { category ->
            ConfirmDeleteItemBottomSheet(
                itemNameToDelete = category.name,
                onDismissRequest = {
                    categoryViewModel.updateCurrentCategory(null)
                    categoryViewModel.expandDeleteCategoryConfirmBottomSheet(false)
                },
                onDeleteConfirm = {
                    categoryViewModel.deleteCategory(category.categoryId)
                }
            )
        }
    }
}


