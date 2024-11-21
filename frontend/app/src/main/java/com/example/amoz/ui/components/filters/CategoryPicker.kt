package com.example.amoz.ui.screens.bottom_screens.products

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.models.CategorySummary

@Composable
fun CategoryPicker(
    category: CategorySummary?,
    onCategoryChange: (CategorySummary) -> Unit,
) {
    val categoryChooseText = stringResource(R.string.product_category_choose)
    val categoryState = category?.name ?: categoryChooseText

    // -------------------- Product's category --------------------
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable { /*TODO*/ },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.Category,
                contentDescription = null
            )
        },
        overlineContent = { Text(stringResource(R.string.product_category)) },
        headlineContent = { Text(categoryState)/*TODO*/ },
        trailingContent = {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )

    )
}