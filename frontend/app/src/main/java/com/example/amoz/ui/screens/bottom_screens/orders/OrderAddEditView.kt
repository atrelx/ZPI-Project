package com.example.amoz.ui.screens.bottom_screens.orders

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.example.amoz.data.ProductVariant
import com.example.amoz.firebase.SoldProduct
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@SuppressLint("MutableCollectionMutableState", "UnrememberedMutableState")
@Composable
fun OrderAddEditVIew(
    saleProduct: SoldProduct,
    closeAddEditDialog: () -> Unit,
    onComplete: (SoldProduct) -> Unit,
    productList: List<ProductVariant> ) {
        var productName by remember { mutableStateOf(saleProduct.name) }
        var salePriceValue by remember { mutableStateOf(saleProduct.salePrice.toString() ) }
        var saleDateValue by remember { mutableStateOf(saleProduct.saleDate) }
        var features by remember { mutableStateOf(saleProduct.characteristics.toList()) }
        var amountValue by remember { mutableStateOf(saleProduct.amount.toString()) }
        var totalPrice by remember { mutableStateOf(saleProduct.totalPrice.toString()) }

        var imageUrl by remember { mutableStateOf(saleProduct.imageUrl) }

        val focusRequester = remember { FocusRequester() }
        var expanded by remember { mutableStateOf(false) }

        // Function to update the total price
        fun updateTotalPrice() {
            val price = salePriceValue.toDoubleOrNull() ?: 0.0
            val amount = amountValue.toIntOrNull() ?: 1
            totalPrice = (price * amount).toString()
        }

        val interactionSource = remember {
            object : MutableInteractionSource {
                override val interactions = MutableSharedFlow<Interaction>(
                    extraBufferCapacity = 16,
                    onBufferOverflow = BufferOverflow.DROP_OLDEST,
                )

                override suspend fun emit(interaction: Interaction) {
                    if (interaction is PressInteraction.Release) {
                        expanded = true
                    }
                    interactions.emit(interaction)
                }

                override fun tryEmit(interaction: Interaction): Boolean {
                    return interactions.tryEmit(interaction)
                }
            }
        }

        // Date Picker Dialog
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                saleDateValue = calendar.timeInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        Dialog(
            onDismissRequest = { closeAddEditDialog() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Box {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title and close button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = if (saleProduct.id.isEmpty()) "Add Product" else "Update Product",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "",
                                tint = colorResource(R.color.darker_gray),
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp)
                                    .clickable { closeAddEditDialog() }
                            )
                        }

                        // Product's name and price
                        Box {
                            var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                                    .onGloballyPositioned { coordinates ->
                                        textFieldSize = coordinates.size
                                    }
                                    .testTag("productName"),  // Add testTag for product name
                                placeholder = { Text(text = "Product name") },
                                label = { Text(text = "Product name") },
                                value = productName,
                                readOnly = true,
                                enabled = (saleProduct.id.isEmpty()),
                                leadingIcon = {
                                    if (imageUrl.isNotEmpty()) {
                                        ProductImage(imageUrl, 32.dp)
                                    }
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.KeyboardArrowDown,
                                        contentDescription = "Product name textfield icon"
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusRequester.requestFocus() }
                                ),
                                interactionSource = interactionSource,
                                onValueChange = {
                                    productName = it
                                },
                                maxLines = 1,
                                singleLine = true
                            )

//                            ProductDropDownMenu(
//                                expanded = expanded,
//                                onDismiss = { expanded = false },
//                                onProductClick = {product ->
//                                    productName = product.name
//                                    salePriceValue = product.impactOnPrice.toString()
//                                    features = product.attributes.toList()
//                                    imageUrl = product.image
//                                    expanded = false
//                                    updateTotalPrice()
//                                    Log.d("-----------------ASD---------------", "DropdownItem_${product.name.replace(" ", "_")}")
//                                },
//                                productList = productList,
//                                modifier = Modifier.width(with(LocalDensity.current) {
//                                    textFieldSize.width.toDp() })
//                            )
                        }

                        // Row for price per item and amount
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .weight(3f)
                                    .focusRequester(focusRequester)
                                    .testTag("pricePerItem"),  // Add testTag for price per item
                                value = salePriceValue,
                                onValueChange = {
                                    if (it.isEmpty() || it.toDoubleOrNull() != null) {
                                        salePriceValue = it
                                        updateTotalPrice()
                                    }
                                },
                                label = { Text(text = "Price per item") },
                                placeholder = { Text(text = "Enter price per item") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusRequester.requestFocus() }
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Money,
                                        contentDescription = "Price",
                                    )
                                },
                                maxLines = 1,
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                            // Amount of sold product
                            OutlinedTextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("amount"),  // Add testTag for amount
                                value = amountValue,
                                onValueChange = {
                                    if (it.isEmpty() || it.toIntOrNull() != null) {
                                        amountValue = it
                                        updateTotalPrice()
                                    }
                                },
                                label = { Text(text = "Amount") },
                                placeholder = { Text(text = "1") },
                                leadingIcon = { Text(text = "x") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusRequester.requestFocus() }
                                ),
                                maxLines = 1,
                                singleLine = true
                            )
                        }

                        // OutlinedTextField for total price
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .testTag("totalPrice"),  // Add testTag for total price
                            value = totalPrice,
                            onValueChange = {
                                if (it.isEmpty() || it.toDoubleOrNull() != null) {
                                    totalPrice = it
                                }
                            },
                            label = { Text(text = "Total Price") },
                            placeholder = { Text(text = "Enter total price") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusRequester.requestFocus() }
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Money,
                                    contentDescription = "Total Price",
                                )
                            },
                            maxLines = 1,
                            singleLine = true
                        )

                        // OutlinedTextField for sale date
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { datePickerDialog.show() }
                                .testTag("saleDate"),
                            value = dateFormat.format(saleDateValue),
                            onValueChange = {},
                            label = { Text(text = "Sold on:") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.DateRange,
                                    contentDescription = "Date Picker",
                                    modifier = Modifier.clickable { datePickerDialog.show() }
                                )
                            },
                            maxLines = 1,
                            singleLine = true
                        )

                        features.forEachIndexed{ index, (featureName, featurePrice) ->
                            FeatureSalesTextField(
                                index,
                                featureName,
                                featurePrice.toString(),
                                focusRequester,
                                onChange = { newPrice ->
                                    features = features.toMutableList().apply {
                                        set(index, featureName to newPrice)
                                    }
                                }
                            )
                        }

                        // Complete adding
                        Button(
                            onClick = {
                                onComplete(saleProduct.copy(
                                    name = productName,
                                    salePrice = salePriceValue.toDouble(),
                                    saleDate = saleDateValue,
                                    imageUrl = imageUrl,
                                    amount = amountValue.toInt(),
                                    totalPrice = totalPrice.toDouble(),
                                    characteristics = features.toMap()
                                    )
                                )
                                closeAddEditDialog()
                            },
                            enabled = productName.isNotBlank()
                                    && salePriceValue.isNotBlank()
                                    && salePriceValue.toDoubleOrNull() != null,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Complete")
                        }
                    }
                }
            }
        }
    }

@Composable
fun ProductDropDownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onProductClick: (ProductVariant) -> Unit,
    productList: List<ProductVariant>,
    modifier: Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss() },
        modifier = modifier
    ) {
        productList.forEach { product ->
            DropdownMenuItem(
                onClick = { onProductClick(product) },
                modifier = Modifier.testTag("DropdownItem_${product.name.replace(" ", "_")}"),  // Add testTag
                text = { Text(text = product.name) },
                leadingIcon = {
//                    if (product.image.isNotEmpty()) {
//                        ProductImage(product.image)
//                    }
                }
            )
        }
    }
}

@Composable
fun ProductImage(productImageUrl: String, imageSize: Dp = 24.dp) {
    Image(
        painter = rememberAsyncImagePainter(productImageUrl),
        contentDescription = "Product Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(imageSize)
            .clip(RoundedCornerShape(4.dp))
    )
}

@Composable
fun FeatureSalesTextField(
    index: Int,
    item: String,
    price: String,
    focusRequester: FocusRequester,
//    onDelete: (Int) -> Unit,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = price,
        onValueChange = { newValue -> onChange(newValue) },
        placeholder = { Text(text = "What is the cost of your feature") },
        label = { Text(item) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusRequester.requestFocus() }
        ),
//        trailingIcon = {
//            IconButton(onClick = { onDelete(index) }) {
//                Icon(
//                    imageVector = Icons.Outlined.Clear,
//                    contentDescription = "Delete textField"
//                )
//            }
//        }
    )
}