package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.amoz.api.requests.ProductCreateRequest

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