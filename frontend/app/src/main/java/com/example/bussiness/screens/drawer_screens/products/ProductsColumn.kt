package com.example.bussiness.screens.drawer_screens.products

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.bussiness.R
import com.example.bussiness.firebase.Product

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsLazyColumn(
    paddingValues: PaddingValues,
    productList: List<Product>,
    onProductClick: (Product) -> Unit,
    onProductLongClick: (Product) -> Unit
) {
    val currencySymbol = stringResource(R.string.currency)
    val showDeleteItemDialog = remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        items(productList) { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .combinedClickable(
                        onClick = { onProductClick(product) },
                        onLongClick = {
                            productToDelete = product
                            showDeleteItemDialog.value = true
                        }
                    )
            ) {
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    headlineContent = { Text(text = product.name, fontSize = 18.sp) },
                    leadingContent = {
                        if (product.imageUrl.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(product.imageUrl),
                                contentDescription = "Product Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    },
                    trailingContent = {
                        Text(
                            text = "${product.price} $currencySymbol",
                            fontSize = 14.sp
                        )
                    },
                )
            }
        }
    }

    if (showDeleteItemDialog.value) {
        productToDelete?.let { product ->
            DeleteItemDialog(
                showDialog = showDeleteItemDialog,
                productName = product.name,
                onDeleteConfirm = {
                    onProductLongClick(product)
                    showDeleteItemDialog.value = false
                }
            )
        }
    }
}

@Composable
fun DeleteItemDialog(
    showDialog: MutableState<Boolean>,
    productName: String,
    onDeleteConfirm: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            icon = {
                   Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete Item")
            },
            title = { Text(text = "Delete product '${productName.trim()}' ?") },
            text = { Text(text = "This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteConfirm()
                        showDialog.value = false
                    }
                ) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}