package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.requests.AttributeCreateRequest
import com.example.amoz.models.Attribute
import com.example.amoz.models.ProductAttribute
import com.example.amoz.ui.HorizontalDividerWithText
import java.util.UUID

@Composable
fun AttributesList(
    productAttributes: List<AttributeCreateRequest>,
    onAttributesChange: (List<AttributeCreateRequest>) -> Unit,
) {
    var productAttributesState by remember { mutableStateOf(productAttributes) }

    HorizontalDividerWithText(stringResource(R.string.product_attributes))

    // -------------------- Product attributes --------------------
    productAttributesState.forEachIndexed { index, productAttribute ->
        productAttribute.value?.let { value ->
            AttributeItem(
                indexInList = index,
                attributeName = productAttribute.attributeName,
                attributeValue = value,
                onDelete = { indexToDelete ->
                    productAttributesState = productAttributesState.toMutableList().apply {
                        removeAt(indexToDelete)
                    }
                    onAttributesChange(productAttributesState)
                },
                onNameChange = { newName ->
                    productAttributesState = productAttributesState.toMutableList().apply {
                        set(index, productAttribute.copy(attributeName = newName)
                        )
                    }
                    onAttributesChange(productAttributesState)
                },
                onValueChange = { newValue ->
                    productAttributesState = productAttributesState.toMutableList().apply {
                        set(index, productAttribute.copy(value = newValue)
                        )
                    }
                    onAttributesChange(productAttributesState)
                }
            )
        }
    }

    // -------------------- Add a feature btn --------------------
    OutlinedButton(
        onClick = {
            productAttributesState = productAttributesState.toMutableList().apply {
                add(
                    AttributeCreateRequest(
                        attributeName = "",
                        value = ""
                    )
                )
            }
            onAttributesChange(productAttributesState)
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
}