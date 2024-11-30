package com.example.amoz.ui.components.pickers

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.models.CategoryTree
import com.example.amoz.ui.screens.Screens
import kotlinx.serialization.json.Json
import java.util.UUID

@Composable
fun <T> CategoryPicker(
    modifier: Modifier = Modifier,
    category: T?,
    leavesOnly: Boolean = false,
    navController: NavController,
    onSaveState: () -> Unit,
    onCategoryChange: (CategoryTree?) -> Unit,
    getCategoryId: (T?) -> UUID?,
    getCategoryName: (T?) -> String?,
) {
    val selectedCategory = remember(category) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedCategoryTree")
            ?.let { Json.decodeFromString(CategoryTree.serializer(), it) }
    }

    LaunchedEffect(selectedCategory) {
        if (selectedCategory != null && selectedCategory.categoryId != getCategoryId(category)) {
            onCategoryChange(selectedCategory)
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selectedCategoryTree")
        }
    }

    val isSelectableCategory = if(leavesOnly) "isSelectableLeavesOnly" else "isSelectable"

    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                onSaveState()
                navController.currentBackStackEntry?.savedStateHandle?.set(isSelectableCategory, true)
                navController.navigate(Screens.Categories.route)
            },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.Category,
                contentDescription = null
            )
        },
        overlineContent = { Text(stringResource(R.string.product_category)) },
        headlineContent = {
            Text(
                text = getCategoryName(category) ?: stringResource(R.string.product_category_choose)
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                category?.let {
                    IconButton(
                        onClick = {
                            onCategoryChange(null)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    }
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    )
}

