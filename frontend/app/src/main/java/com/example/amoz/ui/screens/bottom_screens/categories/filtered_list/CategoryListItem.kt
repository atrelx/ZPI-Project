package com.example.amoz.ui.screens.bottom_screens.categories.filtered_list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.example.amoz.data.Category
import com.example.amoz.ui.theme.extendedColors

@Composable
fun CategoryListItem(
    category: Category,
    onEdit: () -> Unit = {},
    onExpand: () -> Unit,
    isEditable: Boolean = true,
    isChild: Boolean,
    isExpanded: Boolean,
    hasChildren: Boolean
) {

    val childrenListExpandedIcon =
        if(isExpanded) {
            Icons.Default.ArrowCircleUp}
        else {
            Icons.Default.ArrowCircleDown}

    val childrenListExpandedIconTint =
        if(isExpanded) {
            MaterialTheme.extendedColors.customColor2.color}
        else {
            MaterialTheme.colorScheme.outline}

    val borderColor =
        if(!hasChildren) MaterialTheme.colorScheme.outline
        else Color.Transparent

    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                brush = SolidColor(borderColor),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                onExpand()
            },
        leadingContent = {
            if (!isChild) {
                Icon(
                    imageVector = Icons.Filled.Category,
                    contentDescription = null
                )
            }
        },
        headlineContent = { Text(category.name) },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isEditable) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "edit category"
                        )
                    }
                }
                if (hasChildren) {
                    Icon(
                        imageVector = childrenListExpandedIcon,
                        contentDescription = null,
                        tint = childrenListExpandedIconTint
                    )
                }
            }
        },
        colors = ListItemDefaults.colors(
            containerColor =
            if (hasChildren) MaterialTheme.colorScheme.surfaceContainer
            else Color.Transparent
        ),
    )
}