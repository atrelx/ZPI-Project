package com.example.amoz.ui.screens.categories.filtered_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.models.CategoryTree
import com.example.amoz.ui.commonly_used_components.SearchTextField

@Composable
fun CategoriesFilteredList(
    categories: List<CategoryTree>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onEdit: (CategoryTree) -> Unit,
    onAdd: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            SearchTextField(
                searchQuery = searchQuery,
                placeholder = stringResource(id = R.string.search_categories_placeholder),
                onSearchQueryChange = onSearchQueryChange,
                isMoreFiltersVisible = false,
            )
        }
        items(categories, key = { it.categoryId } ) { category ->
            Box(modifier = Modifier.animateItem()) {
                CategoryWithChildren(
                    category = category,
                    allCategories = categories,
                    onEdit = onEdit,
                )
            }
        }
        item {
            // -------------------- Add a category btn --------------------
            OutlinedButton(
                onClick = onAdd,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null
                )
                Text(text = stringResource(id = R.string.add_category_title))
            }
        }
    }
}