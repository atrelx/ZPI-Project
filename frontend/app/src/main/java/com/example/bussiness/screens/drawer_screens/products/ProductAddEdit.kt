package com.example.bussiness.screens.drawer_screens.products

import android.R
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.example.bussiness.firebase.Product

@SuppressLint("MutableCollectionMutableState", "UnrememberedMutableState")
@Composable
fun ProductAddEditDetailsVIew(
    product: Product,
    onComplete: (Product) -> Unit,
    showAddEditDialog: (Boolean) -> Unit,
    onImageUpload: (Uri, (String) -> Unit) -> Unit )
     {
        var productName by remember { mutableStateOf(product.name) }
        var productPrice by remember { mutableStateOf(product.price.toString()) }
        var productImage by remember { mutableStateOf(product.imageUrl) }
        var productFeatures by remember { mutableStateOf(product.features.toList()) }

        var isUploading by remember { mutableStateOf(false) }

        val focusRequester = remember { FocusRequester() }

        val isFormValid = derivedStateOf {
            productName.isNotBlank() && productPrice.isNotBlank()
        }

        val showFeatureDialog = remember { mutableStateOf(false) }

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    isUploading = true
                    onImageUpload(it) { url ->
                        productImage = url
                        isUploading = false
                    }
                }
            }
        )

        Dialog(
            onDismissRequest = { showAddEditDialog(false) },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box {

                    Column(modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        // -------------------- Title and close btn --------------------
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = if (product.name.isEmpty()) "Add Product" else "Update Product",
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
                                    .clickable { showAddEditDialog(false) }
                            )
                        }
                        // ------------------------------------------------------------

                        // -------------------- Product's properties --------------------
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ProductImagePicker(
                                isUploading = isUploading,
                                imageUrl = productImage,
                                onImageClick = { imagePickerLauncher.launch("image/*") },
                                onLongClick = { productImage = "" }
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            // -------------------- Product's name --------------------
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                placeholder = { Text(text = "Pizza Peperoni, Bouquet, etc...") },
                                label = { Text(text = "Product name") },
                                value = productName,
                                leadingIcon = {
                                    Icon(imageVector = Icons.Outlined.Edit,
                                        contentDescription = "Product name textfield icon")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusRequester.requestFocus() }
                                ),
                                onValueChange = {
                                    productName = it
                                },
                                maxLines = 1,
                                singleLine = true
                            )
                        }

                        // -------------------- Product's price --------------------
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            value = productPrice,
                            onValueChange = { productPrice = it },
                            label = { Text(text = "Price") },
                            placeholder = { Text(text = "Enter price") },
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
                            trailingIcon = {
                                Text(text = stringResource(com.example.bussiness.R.string.currency))
                            },
                            maxLines = 1,
                            singleLine = true
                        )

                        HorizontalDivider()

                        // -------------------- Product's custom features --------------------
                        productFeatures.forEachIndexed {
                            index, (featureName, featurePrice) ->
                                FeatureRow(
                                    index = index,
                                    featureName = featureName,
                                    featurePrice = featurePrice.toString(),
                                    onDelete = { indexToDelete ->
                                        productFeatures = productFeatures.toMutableList().apply {
                                            removeAt(indexToDelete)
                                        }
                                    },
                                    onNameChange = { newName ->
                                        productFeatures = productFeatures.toMutableList().apply {
                                            set(index, newName to featurePrice)
                                        }
                                    },
                                    onPriceChange = { newPrice ->
                                        productFeatures = productFeatures.toMutableList().apply {
                                            set(index, featureName to newPrice)
                                        }
                                    }
                                )
                            }

                        // -------------------- Add a feature btn --------------------
                        OutlinedButton(
                            onClick = {
                                showFeatureDialog.value = true
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Add a new feature")
                        }

                        if (showFeatureDialog.value) {
                            FeatureDialog(showFeatureDialog) { featureName, featureType ->
                                if (featureType == FeatureType.TEXT_FIELD) {
                                    productFeatures = productFeatures.toMutableList().apply {
                                        add(featureName to 0.0)
                                    }
                                }
                            }
                        }

                        // -------------------- Complete adding --------------------
                        Button(
                            onClick = {
                                onComplete(product.copy(
                                    name = productName,
                                    price = productPrice.toDoubleOrNull() ?: 0.0,
                                    imageUrl = productImage,
                                    features = productFeatures.toMap()
                                ))
                                showAddEditDialog(false)
                            },
                            enabled = isFormValid.value,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImagePicker(
    isUploading: Boolean,
    imageUrl: String,
    onImageClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box(modifier = Modifier.size(60.dp)) {
        if (isUploading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            if (imageUrl.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .combinedClickable(
                            onClick = onImageClick,
                            onLongClick = onLongClick
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .combinedClickable(
                            onClick = onImageClick,
                            onLongClick = onLongClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Default Image Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}


