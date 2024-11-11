package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.amoz.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AttributeItem(
    indexInList: Int,
    attributeName: String,
    attributeValue: String,
    onDelete: (Int) -> Unit,
    onNameChange: (String) -> Unit,
    onValueChange: (String) -> Unit
) {
    val defaultAttributeNameText = stringResource(R.string.product_attributes_choose_name)
    val defaultAttributeValueText = stringResource(R.string.product_attributes_choose_value)

    val attributeNameState by remember {
        mutableStateOf(
            attributeName.takeIf { it.isNotBlank() } ?: defaultAttributeNameText
        )
    }


    val listItemColors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(10.dp)
            )
        ,
    ) {
        // -------------------- Product attribute name --------------------
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { /*TODO*/ },
                    onLongClick = { onDelete(indexInList) }
                ),
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.CheckBoxOutlineBlank,
                    contentDescription = null
                )
            },
            headlineContent = { Text(attributeNameState) },
            trailingContent = {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null
                )
            },
            colors = listItemColors
        )
        HorizontalDivider()

        // -------------------- Product attribute value --------------------
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(defaultAttributeValueText) },
            value = attributeValue,
            onValueChange = { onValueChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.CheckBox,
                    contentDescription = null
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null
                )
            },
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )

        )
    }
}