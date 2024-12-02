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
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.ui.screens.Screens
import kotlinx.serialization.json.Json

@Composable
fun ProductVariantPickerWithListItem(
    modifier: Modifier = Modifier,
    onProductVariantChange: (ProductVariantDetails) -> Unit,
    onSaveState: () -> Unit,
    navController: NavController
) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    val selectedProductVariant = remember {
        savedStateHandle?.get<String>(SavedStateHandleKeys.SELECTED_PRODUCT_VARIANT_DETAILS)?.let {
            Json.decodeFromString(ProductVariantDetails.serializer(), it)
        }
    }

    LaunchedEffect(selectedProductVariant) {
        if (selectedProductVariant != null) {
            onProductVariantChange(selectedProductVariant)
            savedStateHandle?.remove<String>(SavedStateHandleKeys.SELECTED_PRODUCT_VARIANT_DETAILS)
        }
    }

    ProductVariantPickerListItem(
        modifier = modifier,
        variantName = selectedProductVariant?.variantName,
        onClick = {
            onSaveState()
            savedStateHandle?.set(SavedStateHandleKeys.SHOW_NAV_ELEMENTS, false)
            savedStateHandle?.set(SavedStateHandleKeys.PRODUCT_VARIANT_PICKER_MODE, true)
            navController.navigate(Screens.Products.route)
        }
    )

}

@Composable
fun ProductVariantPickerListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    variantName: String?
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable { onClick() },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.Category,
                contentDescription = null
            )
        },
        overlineContent = { Text(stringResource(R.string.products_variant)) },
        headlineContent = {
            Text(
                text = variantName ?: stringResource(R.string.products_choose_product_variant)
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