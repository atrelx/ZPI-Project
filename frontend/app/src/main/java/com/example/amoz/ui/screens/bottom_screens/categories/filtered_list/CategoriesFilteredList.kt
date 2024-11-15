package com.example.amoz.ui.screens.bottom_screens.categories.filtered_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.amoz.data.Category

@Composable
fun CategoriesFilteredList(
    categoryList: List<Category>,
) {
    val categoriesTree = categoryList.groupBy { it.parentCategoryId }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categoriesTree[""] ?: emptyList()) { rootCategory ->
            Box(modifier = Modifier.animateItem()) {
                CategoryWithChildren(
                    category = rootCategory,
                    categoriesTree = categoriesTree
                )
            }
        }
    }
}