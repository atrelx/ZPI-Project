package com.example.amoz.ui.screens.bottom_screens.categories

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amoz.R
import com.example.amoz.ui.screens.bottom_screens.categories.filtered_list.CategoriesFilteredList
import com.example.amoz.view_models.CategoriesViewModel

@Composable
fun CategoriesScreen(
    paddingValues: PaddingValues,
    categoryViewModel: CategoriesViewModel = viewModel()
) {
    val categoryUiState by categoryViewModel.categoriesUiState.collectAsState()

    fun closeAddEditCategoryBottomSheet() {
        categoryViewModel.updateCurrentAddEditCategory(null)
        categoryViewModel.expandAddEditCategoryBottomSheet(false)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        CategoriesFilteredList(
            categories = categoryUiState.filteredCategoryList,
            searchQuery = categoryUiState.searchQuery,
            onSearchQueryChange = categoryViewModel::updateSearchQuery,
            onEdit = { category ->
                categoryViewModel.updateCurrentAddEditCategory(category)
                categoryViewModel.expandAddEditCategoryBottomSheet(true)
            },
            onAdd = {
                categoryViewModel.expandAddEditCategoryBottomSheet(true)
            }
        )

        // -------------------- Bottom sheet title --------------------
        Box(modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            ExtendedFloatingActionButton(
                onClick = {
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
            AddEditCategoryBottomSheet(
                category = categoryUiState.currAddEditCategory,
                onDismissRequest = {
                    closeAddEditCategoryBottomSheet()
                },
                onComplete = { name, subcategoriesList ->
                    categoryUiState.currAddEditCategory?.let { category ->
                        categoryViewModel.updateCategory(category.categoryId, name)
                        categoryViewModel.addSubcategoriesToCategory(subcategoriesList)
                    } ?: run {
                        categoryViewModel.addCategory(name)
                        /*TODO: add subcategories*/
                    }
                },
                onSubcategoryEdit = categoryViewModel::updateCurrentAddEditCategory
            )
    }
}


