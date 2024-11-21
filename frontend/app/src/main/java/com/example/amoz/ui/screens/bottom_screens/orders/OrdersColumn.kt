//package com.example.amoz.ui.screens.bottom_screens.orders
//
//import android.util.Log
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.combinedClickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material.icons.outlined.Clear
//import androidx.compose.material.icons.outlined.MoreVert
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.ListItem
//import androidx.compose.material3.ListItemDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.material3.rememberModalBottomSheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.rememberAsyncImagePainter
//import com.example.amoz.R
//import com.example.amoz.firebase.FirebaseRepository
//import com.example.amoz.firebase.SoldProduct
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
//@Composable
//fun OrdersLazyColumn(
//    paddingValues: PaddingValues,
//    salesList: List<SoldProduct>,
//    filterBottomSheetShowed: Boolean,
//    onSoldProductClick: (SoldProduct) -> Unit,
//    updateFilterBottomSheetShowState: (Boolean) -> Unit
//) {
//    val currencySymbol = stringResource(R.string.currency)
//    var showDeleteSaleDialog by remember { mutableStateOf(false) }
//    val saleToDelete = remember { mutableStateOf<SoldProduct?>(null) }
//    var searchQuery by remember { mutableStateOf("") }
//
//    var priceFrom by remember { mutableStateOf(0f) }
//    var priceTo by remember { mutableStateOf(1000f) }
//    var startDate by remember { mutableStateOf<Long?>(null) }
//    var endDate by remember { mutableStateOf<Long?>(null) }
//
//    // Filter and sort the sales list
//    val filteredAndSortedSalesList = salesList
//        .filter { it.name.contains(searchQuery, ignoreCase = true) }
//        .filter {
//            val priceFilter = (priceFrom == 0f || it.totalPrice >= priceFrom) &&
//                    (priceTo == 1000f || it.totalPrice <= priceTo)
//            val dateFilter = (startDate == null || it.saleDate >= startDate!!) &&
//                    (endDate == null || it.saleDate <= endDate!!)
//            priceFilter && dateFilter
//        }
//        .sortedByDescending { it.saleDate }
//
//    // Group items by date
//    val groupedSalesList = filteredAndSortedSalesList.groupBy {
//        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(it.saleDate)
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        LazyColumn(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//        ) {
//            item {
//                OutlinedTextField(
//                    value = searchQuery,
//                    onValueChange = { searchQuery = it },
//                    placeholder = { Text(text = "Hinted search text") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    singleLine = true,
//                    maxLines = 1,
//                    trailingIcon = {
//                        Row {
//                            if (searchQuery.isNotEmpty()) {
//                                IconButton(onClick = { searchQuery = "" }) {
//                                    Icon(imageVector = Icons.Outlined.Clear, contentDescription = "Clear Search")
//                                }
//                            }
//                            IconButton(onClick = { updateFilterBottomSheetShowState(true) }) {
//                                Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "More filters")
//                            }
//                        }
//                    },
//                    shape = RoundedCornerShape(15.dp)
//                )
//            }
//
//            groupedSalesList.forEach { (date, sales) ->
//                // Add header as a regular item with padding
//                item {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 5.dp)
//                            .border(1.dp, Color.DarkGray, RoundedCornerShape(15.dp))
//                            .background(MaterialTheme.colorScheme.background)
//                    ) {
//                        Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
//                            Icon(
//                                modifier = Modifier
//                                    .align(Alignment.CenterVertically)
//                                    .padding(start = 15.dp),
//                                imageVector = Icons.Filled.DateRange,
//                                contentDescription = "Date Picker",
//                            )
//                            Text(
//                                text = date,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .background(MaterialTheme.colorScheme.background)
//                                    .padding(horizontal = 16.dp, vertical = 8.dp),
//                            )
//                        }
//                    }
//                }
//                items(sales) { sale ->
//                    Log.d("ASD", "ProductItem_${sale.id}")
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 5.dp)
//                            .combinedClickable(
//                                onClick = { onSoldProductClick(sale) },
//                                onLongClick = {
//                                    saleToDelete.value = sale
//                                    showDeleteSaleDialog = true
//                                }
//                            )
//                            .testTag("ProductItem_${sale.id}")
//                    ) {
//                        ListItem(
//                            colors = ListItemDefaults.colors(
//                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
//                            ),
//                            headlineContent = { Text(text = sale.name, fontSize = 18.sp) },
//                            leadingContent = {
//                                if (sale.imageUrl.isNotEmpty()) {
//                                    Image(
//                                        painter = rememberAsyncImagePainter(sale.imageUrl),
//                                        contentDescription = "Sold Product Image",
//                                        contentScale = ContentScale.Crop,
//                                        modifier = Modifier
//                                            .size(60.dp)
//                                            .clip(RoundedCornerShape(10.dp))
//                                    )
//                                }
//                            },
//                            trailingContent = {
//                                val totalCharacteristicsPrice = sale.characteristics.values
//                                    .map { it.toString().toDoubleOrNull() ?: 0.0 }
//                                    .sum()
//
//                                Text(
//                                    text = "${sale.totalPrice + totalCharacteristicsPrice} $currencySymbol",
//                                    fontSize = 14.sp
//                                )
//                            },
//                            supportingContent = {
//                                Text(text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(sale.saleDate), fontSize = 12.sp)
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    if (showDeleteSaleDialog) {
//        saleToDelete.value?.let { product ->
//            DeleteSaleDialog(
//                showDialog = showDeleteSaleDialog,
//                saleProductName = product.name,
//                closeDialog = { showDeleteSaleDialog = false },
//                onDeleteConfirm = { FirebaseRepository.deleteSale(product.id) }
//            )
//        }
//    }
//
//    if (filterBottomSheetShowed) {
//        val sheetState = rememberModalBottomSheetState()
//        val scope = rememberCoroutineScope()
//        ModalBottomSheet(
//            onDismissRequest = { updateFilterBottomSheetShowState(false) },
//            sheetState = sheetState
//        ) {
//            BottomSheetContent(
//                priceFrom = priceFrom,
//                priceTo = priceTo,
//                startDate = startDate,
//                endDate = endDate,
//                onApplyFilters = { fromPrice, toPrice, fromDate, toDate ->
//                    priceFrom = fromPrice
//                    priceTo = toPrice
//                    startDate = fromDate
//                    endDate = toDate
//                    scope.launch { sheetState.hide() }.invokeOnCompletion {
//                        if (!sheetState.isVisible) {
//                            updateFilterBottomSheetShowState(false)
//                        }
//                    }
//                },
//                onCancelFilters = {
//                    priceFrom = 0f
//                    priceTo = 1000f
//                    startDate = null
//                    endDate = null
//                    scope.launch { sheetState.hide() }.invokeOnCompletion {
//                        if (!sheetState.isVisible) {
//                            updateFilterBottomSheetShowState(false)
//                        }
//                    }
//                }
//            )
//        }
//    }
//}
//
//
//@Composable
//fun DeleteSaleDialog(
//    showDialog: Boolean,
//    closeDialog: () -> Unit,
//    saleProductName: String,
//    onDeleteConfirm: () -> Unit
//) {
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { closeDialog() },
//            title = { Text(text = "Delete sale '${saleProductName.trim()}' ?") },
//            text = { Text(text = "Are you sure you want to delete $saleProductName?\n" +
//                    "This action cannot be undone.") },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        onDeleteConfirm()
//                        closeDialog()
//                    }
//                ) {
//                    Text(text = "Delete")
//                }
//                closeDialog()
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = { closeDialog() }
//                ) {
//                    Text(text = "Cancel")
//                }
//            }
//        )
//    }
//}