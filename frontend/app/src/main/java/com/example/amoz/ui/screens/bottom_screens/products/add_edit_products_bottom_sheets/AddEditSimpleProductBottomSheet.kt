package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.amoz.R
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.ui.HorizontalDividerWithText
import com.example.amoz.ui.screens.bottom_screens.products.CategoryPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSimpleProductBottomSheet(
    onDismissRequest: () -> Unit,
    productVariant: ProductCreateRequest,
    onComplete: (ProductCreateRequest) -> Unit,
//    onImageUpload: (Uri, (String) -> Unit) -> Unit
) {
//
//    val productVariantState:
//
//    var isUploading by remember { mutableStateOf(false) }
//
//    val focusRequester = remember { FocusRequester() }
//
//    val isFormValid by remember {
//        derivedStateOf {
//            productName.isNotBlank() && productPrice.isNotBlank()
//        }
//    }
//
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        onResult = { uri ->
//            uri?.let {
//                isUploading = true
////                onImageUpload(it) { url ->
////                    productImage = url
////                    isUploading = false
////                }
//            }
//        }
//    )
//    val listItemColors = ListItemDefaults.colors(
//        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
//    )
//    val listItemModifier = Modifier
//        .fillMaxWidth()
//        .clip(RoundedCornerShape(5.dp))
//        .border(
//            width = 1.dp,
//            brush = SolidColor(MaterialTheme.colorScheme.outline),
//            shape = RoundedCornerShape(5.dp)
//        )
//    val sheetState =
//        rememberModalBottomSheetState(
//            skipPartiallyExpanded = true,
//            confirmValueChange = { newState ->
//                newState != SheetValue.Hidden
//            }
//        )
//
//    ModalBottomSheet(
//        onDismissRequest = onDismissRequest,
//        sheetState = sheetState,
//) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(10.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//
//            // -------------------- Bottom sheet title --------------------
//            Text(
//                text = stringResource(R.string.products_add_simple_product),
//                style = MaterialTheme.typography.headlineSmall
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//
//            // -------------------- Product's properties --------------------
//            Row(
//                verticalAlignment = Alignment.Bottom,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                ProductImagePicker(
//                    isUploading = isUploading,
//                    image = productImage,
//                    onImageClick = { imagePickerLauncher.launch("image/*") },
//                    onLongClick = { productImage = null }
//                )
//
//                Spacer(modifier = Modifier.width(10.dp))
//
//                // -------------------- Product's name --------------------
//                OutlinedTextField(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .focusRequester(focusRequester),
//                    label = { Text(stringResource(R.string.product_name)) },
//                    value = productName,
//                    onValueChange = { productName = it },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Outlined.Edit,
//                            contentDescription = null
//                        )
//                    },
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Text,
//                        imeAction = ImeAction.Next
//                    ),
//                    keyboardActions = KeyboardActions(
//                        onNext = { focusRequester.requestFocus() }
//                    ),
//
//                    maxLines = 1,
//                    singleLine = true
//                )
//            }
//
//            // -------------------- Product's description --------------------
////            OutlinedTextField(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .height(100.dp)
////                    .focusRequester(focusRequester),
////                value = productDescription,
////                onValueChange = { productDescription = it },
////                label = { Text(stringResource(R.string.product_description)) },
////                keyboardOptions = KeyboardOptions(
////                    keyboardType = KeyboardType.Text,
////                    imeAction = ImeAction.Next
////                ),
////                keyboardActions = KeyboardActions(
////                    onNext = { focusRequester.requestFocus() }
////                )
////            )
//
//            // -------------------- Product's price --------------------
//            OutlinedTextField(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .focusRequester(focusRequester),
//                value = productPrice,
//                onValueChange = {
//                    if(it.isDigitsOnly()){
//                        productPrice = it
//                    }
//                },
//                label = { Text(stringResource(R.string.product_price)) },
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Next
//                ),
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Filled.CreditCard,
//                        contentDescription = null,
//                    )
//                },
//                trailingIcon = {
//                    Text(text = stringResource(com.example.amoz.R.string.currency))
//                },
//                maxLines = 1,
//                singleLine = true
//            )
//
//            // -------------------- Product's unit --------------------
//            ListItem(
//                modifier = listItemModifier.then(Modifier
//                    .clickable { /*TODO*/ })
//                ,
//                leadingContent = { Icon(
//                    imageVector = Icons.Outlined.PieChart,
//                    contentDescription = null
//                ) },
//                overlineContent = { Text(stringResource(R.string.product_unit)) },
//                headlineContent = { Text(text = "liters") /*TODO*/ },
//                trailingContent = { Icon(
//                    imageVector = Icons.Outlined.KeyboardArrowDown,
//                    contentDescription = null
//                ) },
//                colors = listItemColors
//
//            )
//
//            // -------------------- Product's category --------------------
//            CategoryPicker(
//                category = productCategory,
//                onCategoryChange = { productCategory = it }
//            )
//
//            // -------------------- Product's template --------------------
//            ListItem(
//                modifier = listItemModifier.then(Modifier
//                    .clickable { /*TODO*/ })
//                ,
//                leadingContent = { Icon(
//                    imageVector = Icons.Outlined.Description,
//                    contentDescription = null
//                ) },
//                overlineContent = { Text(stringResource(R.string.product_template)) },
//                headlineContent = { Text(stringResource(R.string.product_template_choose))/*TODO*/ },
//                trailingContent = { Icon(
//                    imageVector = Icons.Outlined.KeyboardArrowDown,
//                    contentDescription = null
//                ) },
//                colors = listItemColors
//
//            )
//
//            HorizontalDividerWithText(stringResource(R.string.product_stock))
//
//            // -------------------- Product's amount in stock --------------------
//            ListItem(
//                modifier = listItemModifier,
//                leadingContent = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            imageVector = Icons.Outlined.Remove,
//                            contentDescription = null
//                        )
//                    }
//                },
//                headlineContent = {    /*TODO*/
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center) {
//                        Text(text = "5")
//                    }
//                },
//                trailingContent = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            imageVector = Icons.Outlined.Add,
//                            contentDescription = null
//                        )
//                    }
//                },
//                colors = listItemColors
//            )
//
//            HorizontalDividerWithText(stringResource(R.string.product_attributes))
//
//            // -------------------- Product's custom features --------------------
//            productFeatures.forEachIndexed { index, (attributeName, attributeValue) ->
//                AttributeItem(
//                    indexInList = index,
//                    attributeName = attributeName,
//                    attributeValue = attributeValue,
//                    onDelete = { indexToDelete ->
//                        productFeatures = productFeatures.toMutableList().apply {
//                            removeAt(indexToDelete)
//                        }
//                    },
//                    onNameChange = { newName ->
//                        productFeatures = productFeatures.toMutableList().apply {
//                            set(index, newName to attributeValue)
//                        }
//                    },
//                    onValueChange = { newPrice ->
//                        productFeatures = productFeatures.toMutableList().apply {
//                            set(index, attributeName to newPrice)
//                        }
//                    }
//                )
//            }
//
//            // -------------------- Add a feature btn --------------------
//            OutlinedButton(
//                onClick = {
//                    productFeatures = productFeatures.toMutableList().apply {
//                        add("" to "")
//                    }
//                },
//                shape = RoundedCornerShape(10.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//            ) {
//                Icon(
//                    imageVector = Icons.Outlined.Add,
//                    contentDescription = null
//                )
//                Text(text = "Add a new feature")
//            }
//
//            Spacer(modifier = Modifier.height(15.dp))
//
//            // -------------------- Complete adding --------------------
//            Button(
//                onClick = {
//                    onComplete(
//                        productVariant.copy(
//                            name = productName,
//                            impactOnPrice = productPrice.toDouble(),
//                            image = productImage,
//                            attributes = productFeatures.toMap()
//                        )
//                    )
//                },
//                enabled = isFormValid,
//                shape = RoundedCornerShape(10.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//            ) {
//                Text(text = "Complete")
//            }
//
//            // -------------------- Close bottom sheet --------------------
//            OutlinedButton(
//                onClick = {
//                    onDismissRequest()
//                },
//                shape = RoundedCornerShape(10.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//            ) {
//                Text(
//                    text = "Close",
//                    color = MaterialTheme.colorScheme.error
//                )
//            }
//        }
//    }
}