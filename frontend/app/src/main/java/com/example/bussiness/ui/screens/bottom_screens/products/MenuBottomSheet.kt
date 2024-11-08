package com.example.bussiness.ui.screens.bottom_screens.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bussiness.app.productScreenBottomSheetMenu
import com.example.bussiness.data.NavItem
import com.example.bussiness.ui.BottomSheetNavigationRaw

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheet(
    onDismissRequest: () -> Unit,
    menuBottomSheetNavItems: List<NavItem> = productScreenBottomSheetMenu.values.toList()
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            menuBottomSheetNavItems.forEachIndexed { index, navItem ->
                BottomSheetNavigationRaw(
                    leadingIcon = navItem.icon,
                    text = stringResource(id = navItem.title),
                    onClick = {}
                )
                if (index == 2 || index == 4) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }

        }
    }
}