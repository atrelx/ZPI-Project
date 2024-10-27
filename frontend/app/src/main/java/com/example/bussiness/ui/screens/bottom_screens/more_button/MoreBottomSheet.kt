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
import androidx.navigation.NavHostController
import com.example.bussiness.app.NavigationItem
import com.example.bussiness.app.moreBottomSheetItems


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreBottomSheet(
    hideMoreBottomSheet: () -> Unit,
    onClick: (NavigationItem) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { hideMoreBottomSheet() }
    )
    {
        Column {
            moreBottomSheetItems.forEach { item ->
                BottomSheetNavigationRaw(
                    icon = item.selectedIcon,
                    text = stringResource(item.title),
                    onClick = { onClick(item) }
                )
            }
        }
    }
}
@Composable
fun BottomSheetNavigationRaw(
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