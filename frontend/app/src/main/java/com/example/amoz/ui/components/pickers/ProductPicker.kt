package com.example.amoz.ui.components.pickers

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Icon
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
import com.example.amoz.data.SavedStateHandleKeys
import com.example.amoz.models.ProductSummary
import com.example.amoz.ui.screens.Screens
import kotlinx.serialization.json.Json

@Composable
fun ProductPicker(
    modifier: Modifier = Modifier,
    onProductChange: (ProductSummary) -> Unit,
    onSaveState: () -> Unit,
    navController: NavController
) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    val selectedProduct = remember {
        savedStateHandle?.get<String>(SavedStateHandleKeys.SELECTED_PRODUCT_SUMMARY)?.let {
            Json.decodeFromString(ProductSummary.serializer(), it)
        }
    }

    LaunchedEffect(selectedProduct) {
        if (selectedProduct != null) {
            onProductChange(selectedProduct)
            savedStateHandle?.remove<String>(SavedStateHandleKeys.SELECTED_PRODUCT_SUMMARY)
        }
    }

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
                savedStateHandle?.set(SavedStateHandleKeys.SHOW_NAV_ELEMENTS, false)
                savedStateHandle?.set(SavedStateHandleKeys.PRODUCT_PICKER_MODE, true)
                navController.navigate(Screens.Products.route)
            },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.Category,
                contentDescription = null
            )
        },
        overlineContent = { Text(stringResource(R.string.products_variant_of)) },
        headlineContent = {
            Text(
                text = selectedProduct?.name ?: stringResource(R.string.products_choose_product)
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
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

