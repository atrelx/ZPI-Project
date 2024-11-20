package com.example.amoz.ui.commonly_used_components.filters

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.view_models.ProductsViewModel

@Composable
fun ListSorting(
    sortingType: ProductsViewModel.SortingType,
    onSortingTypeChange: (ProductsViewModel.SortingType) -> Unit,
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    val sortingTypeText = when (sortingType) {
        ProductsViewModel.SortingType.ASCENDING_NAME -> stringResource(id = R.string.sorting_type_ascending_name)
        ProductsViewModel.SortingType.DESCENDING_NAME -> stringResource(id = R.string.sorting_type_descending_name)
        ProductsViewModel.SortingType.ASCENDING_PRICE -> stringResource(id = R.string.sorting_type_ascending_price)
        ProductsViewModel.SortingType.DESCENDING_PRICE -> stringResource(id = R.string.sorting_type_descending_price)
        ProductsViewModel.SortingType.NONE -> stringResource(id = R.string.sorting_type_none)
    }

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .border(
                1.dp,
                SolidColor(MaterialTheme.colorScheme.outline),
                RoundedCornerShape(5.dp)
            )
            .clickable { dropDownMenuExpanded = true },
        overlineContent = {
            Text(text = stringResource(id = R.string.sorting_type))
        },
        headlineContent = { Text(sortingTypeText) },
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

    DropdownMenu(
        expanded = dropDownMenuExpanded,
        onDismissRequest = { dropDownMenuExpanded = false }
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.sorting_type_ascending_name)) },
            onClick = {
                onSortingTypeChange(ProductsViewModel.SortingType.ASCENDING_NAME)
                dropDownMenuExpanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.sorting_type_descending_name)) },
            onClick = {
                onSortingTypeChange(ProductsViewModel.SortingType.DESCENDING_NAME)
                dropDownMenuExpanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.sorting_type_ascending_price)) },
            onClick = {
                onSortingTypeChange(ProductsViewModel.SortingType.ASCENDING_PRICE)
                dropDownMenuExpanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.sorting_type_descending_price)) },
            onClick = {
                onSortingTypeChange(ProductsViewModel.SortingType.DESCENDING_PRICE)
                dropDownMenuExpanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.sorting_type_none)) },
            onClick = {
                onSortingTypeChange(ProductsViewModel.SortingType.NONE)
                dropDownMenuExpanded = false
            }
        )
    }
}