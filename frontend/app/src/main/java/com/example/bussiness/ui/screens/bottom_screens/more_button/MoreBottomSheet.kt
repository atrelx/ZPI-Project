package com.example.bussiness.ui.screens.bottom_screens.more_button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.bussiness.data.NavItem
import com.example.bussiness.app.moreBottomSheetItemsMap


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreBottomSheet(
    hideMoreBottomSheet: () -> Unit,
    navigateToScreen: (NavItem) -> Unit,
    moreBottomSheetNavItems: List<NavItem> = moreBottomSheetItemsMap.values.toList()
) {
    ModalBottomSheet(
        onDismissRequest = { hideMoreBottomSheet() }
    )
    {
        Column {
            moreBottomSheetNavItems.forEach { item ->
                MoreBottomSheetNavigationRaw(
                    icon = item.selectedIcon,
                    text = stringResource(item.title),
                    onClick = {
                        hideMoreBottomSheet()
                        navigateToScreen(item)
                    }
                )
            }
        }
    }
}
@Composable
fun MoreBottomSheetNavigationRaw(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier
            .clickable(onClick = onClick),
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        },
        headlineContent = { Text(text = text) },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    )
}