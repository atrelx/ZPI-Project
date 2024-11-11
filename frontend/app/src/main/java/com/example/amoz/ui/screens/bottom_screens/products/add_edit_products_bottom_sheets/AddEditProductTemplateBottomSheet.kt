package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.data.ProductTemplate
import com.example.amoz.ui.commonly_used_components.CloseOutlinedButton
import com.example.amoz.ui.HorizontalDividerWithText
import com.example.amoz.ui.commonly_used_components.PrimaryFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductTemplateBottomSheet(
    onDismissRequest: () -> Unit,
    onComplete: (ProductTemplate) -> Unit,
    product: ProductTemplate,
) {
    var productName by remember { mutableStateOf(product.name) }
    var productDescription by remember { mutableStateOf(product.description) }
    var productPrice by remember { mutableStateOf(product.price) }
    var productVendor by remember { mutableStateOf(product.productVendor) }
    var productFeatures by remember { mutableStateOf(product.attributes.toList()) }

    val isFormValid by remember {
        derivedStateOf {
            productName.isNotBlank() && productPrice.isNotBlank()
        }
    }

    val sheetState =
        rememberModalBottomSheetState( skipPartiallyExpanded = true,
            confirmValueChange = { newState ->
                newState != SheetValue.Hidden
            }
        )

    val listItemColors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    )
    val listItemModifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(5.dp))
        .border(
            width = 1.dp,
            brush = SolidColor(MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(5.dp)
        )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // -------------------- Bottom sheet title --------------------
            Text(
                text = stringResource(R.string.products_add_product_template),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(10.dp))

            // -------------------- Product basic info --------------------
            ProductNameDescriptionPrice(
                productName = productName,
                productDescription = productDescription,
                productPrice = productPrice,
                onNameChange = { productName = it },
                onDescriptionChange = { productDescription = it },
                onPriceChange = { productPrice = it },
            )

            // -------------------- Product vendor --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(stringResource(R.string.product_vendor)) },
                value = productVendor,
                onValueChange = { productVendor = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Product's category --------------------
            ListItem(
                modifier = listItemModifier.then(Modifier
                    .clickable { /*TODO*/ })
                ,
                leadingContent = { Icon(
                    imageVector = Icons.Outlined.Category,
                    contentDescription = null
                ) },
                overlineContent = { Text(stringResource(R.string.product_category)) },
                headlineContent = { Text(stringResource(R.string.product_category_choose))/*TODO*/ },
                trailingContent = { Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null
                ) },
                colors = listItemColors
            )

            HorizontalDividerWithText(stringResource(R.string.product_attributes))

            // -------------------- Product attributes --------------------
            productFeatures.forEachIndexed { index, (attributeName, attributeValue) ->

                AttributeItem(
                    indexInList = index,
                    attributeName = attributeName,
                    attributeValue = attributeValue,
                    onDelete = { indexToDelete ->
                        productFeatures = productFeatures.toMutableList().apply {
                            removeAt(indexToDelete)
                        }
                    },
                    onNameChange = { newName ->
                        productFeatures = productFeatures.toMutableList().apply {
                            set(index, newName to attributeValue)
                        }
                    },
                    onValueChange = { newValue ->
                        productFeatures = productFeatures.toMutableList().apply {
                            set(index, attributeName to newValue)
                        }
                    }
                )
            }

            // -------------------- Add a feature btn --------------------
            OutlinedButton(
                onClick = {
                    productFeatures = productFeatures.toMutableList().apply {
                        add("" to "")
                    }
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null
                )
                Text(stringResource(id = R.string.product_attributes_add_attributes))
            }


            // -------------------- Complete adding --------------------
            Spacer(modifier = Modifier.height(15.dp))

            PrimaryFilledButton(
                onClick = {
                    onComplete(
                        product.copy(
                            name = productName,
                            price = productPrice,
                            productVendor = productVendor,
                            attributes = productFeatures.toMap()
                        )
                    )
                },
                text = stringResource(id = R.string.done),
                enabled = isFormValid
            )
            // -------------------- Close bottom sheet --------------------
            CloseOutlinedButton(onDismissRequest, stringResource(R.string.close))
        }
    }
}