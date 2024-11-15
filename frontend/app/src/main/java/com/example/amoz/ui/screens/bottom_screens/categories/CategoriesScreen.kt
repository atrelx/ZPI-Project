package com.example.amoz.ui.screens.bottom_screens.categories

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amoz.ui.screens.bottom_screens.categories.filtered_list.CategoriesFilteredList

@Composable
fun CategoriesScreen(
    paddingValues: PaddingValues,
    categoryViewModel: CategoriesViewModel = viewModel()
) {
    val categoryUiState by categoryViewModel.categoriesUiState.collectAsState()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        CategoriesFilteredList(
            categoryList = categoryUiState.categoryList
        )
    }
}


