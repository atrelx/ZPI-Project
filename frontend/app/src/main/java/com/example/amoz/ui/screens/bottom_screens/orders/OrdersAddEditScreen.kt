package com.example.amoz.ui.screens.bottom_screens.orders

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.api.enums.Status
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.app.AppPreferences
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.ui.components.bottom_sheets.AddressBottomSheet
import com.example.amoz.ui.components.HorizontalDividerWithTextBefore
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.QuantityBottomSheet
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.components.pickers.ProductVariantPickerWithListItem
import com.example.amoz.ui.components.text_fields.AddressTextField
import com.example.amoz.ui.components.text_fields.DateTextField
import com.example.amoz.ui.screens.bottom_screens.orders.product_items.ProductListItem
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.OrdersViewModel
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersAddEditScreen (
    navController: NavController,
    paddingValues: PaddingValues,
    ordersViewModel: OrdersViewModel
) {
    val ordersUiState by ordersViewModel.orderUiState.collectAsState()
    val dateState = rememberDatePickerState()

    val currContext = LocalContext.current
    val appPreferences = remember { AppPreferences(currContext) }
    val currency by appPreferences.currency.collectAsState(initial = "USD")

    var productVariantList by remember { mutableStateOf(ordersViewModel.currentProductVariantDetailsList) }
    var currentProductVariant by remember { mutableStateOf(ordersViewModel.currentPickedVariant) }

    AmozApplicationTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            ResultStateView(ordersUiState.currentAddEditOrderState) { request ->
                var orderRequest by remember { mutableStateOf(request) }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                ) {

                    // ---------------------------- Status Dropdown ----------------------------
                    item { HorizontalDividerWithTextBefore(text = stringResource(R.string.orders_status)) }

                    item {
                        ExposedDropdownMenuBox(
                            expanded = ordersUiState.isDropdownStatusExpanded,
                            onExpandedChange = { ordersViewModel.changeDropdownStatus(it) }
                        ) {
                            OutlinedTextField(
                                value = orderRequest.status.getName(),
                                onValueChange = { },
                                label = { Text(stringResource(R.string.orders_select_status)) },
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (ordersUiState.isDropdownStatusExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                textStyle = MaterialTheme.typography.bodyLarge
                            )

                            ExposedDropdownMenu(
                                expanded = ordersUiState.isDropdownStatusExpanded,
                                onDismissRequest = { ordersViewModel.changeDropdownStatus(false) }
                            ) {
                                Status.values().forEach { status ->
                                    DropdownMenuItem(
                                        text = { Text(status.getName()) },
                                        onClick = {
                                            orderRequest = orderRequest.copy(status = status)
                                            ordersViewModel.changeDropdownStatus(false)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // ---------------------------- Products List ----------------------------

                    item { HorizontalDividerWithTextBefore(text = stringResource(R.string.orders_products)) }

                    items(
                        productVariantList,
                        key = { it.productVariant.productVariantId }
                    ) { productVariantItem ->
                        ProductListItem(
                            product = productVariantItem,
                            onClick = { },
                            // TODO: consider moving to viewmodel removing product from the list logic
                            onProductRemove = { product ->
                                productVariantList = productVariantList.toMutableList().apply {
                                    remove(product)
                                }
                            },
                            currency = currency!!
                        )
                    }

                    item {
                        ProductVariantPickerWithListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp)),
                            onProductVariantChange = { productVariant ->
                                currentProductVariant = productVariant
                            },
                            onSaveState = {
                                // IDK what to pass here
                            },
                            // todo: add logic for quantity
                            navController = navController,
                        )
                    }

                    // ---------------------------- Customer ListItem ----------------------------

                    item { HorizontalDividerWithTextBefore(text = stringResource(R.string.orders_customer)) }

                    /* TODO решить с андреем могу ли я использовать его компонент
                    для клиентов б2б и б2с */

                    // ---------------------------- Shipping Number, Address, Date ----------------------------

                    item {  HorizontalDividerWithTextBefore(text = stringResource(R.string.orders_shipping)) }

                    item {
                        AddressTextField (
                            modifier = Modifier.height(R.dimen.text_field_height.dp),
                            trailingIcon = Icons.Outlined.ArrowForward,
                            address = orderRequest.address,
                            onClick = {
                                ordersViewModel.changeAddressBottomSheetStatus(true)
                            }
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = orderRequest.trackingNumber ?: "",
                            onValueChange = {
                                if (it.length <= 10) orderRequest =
                                    orderRequest.copy(trackingNumber = it)
                            },
                            label = { Text(stringResource(R.string.orders_tracking_number)) },
                            modifier = Modifier
                                .height(R.dimen.text_field_height.dp)
                                .fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                    }

                    item {
                        DateTextField(
                            label = stringResource(R.string.orders_shipping_date),
                            date = orderRequest.timeOfSending,
                            onDateChange = {
                                orderRequest = orderRequest.copy(timeOfSending = it as? LocalDateTime)
                            },
                            trailingIcon = Icons.Default.DateRange,
                            showTime = true,
                            formatAsDateOnly = false,
                        )
                    }

                    // checks whether its a new order or an existing one and performs the appropriate action
                    item {
                        PrimaryFilledButton(
                            text = stringResource(R.string.orders_create_an_order),
                            onClick = {
                                ordersViewModel.chooseProductOrderOperation(
                                    orderRequest,
                                    productVariantList
                                )
                                navController.popBackStack()
                            }
                        )
                    }

                    if (ordersUiState.isDatePickerVisible) {
                        item {
                            DatePickerDialog(
                                onDismissRequest = { ordersViewModel.changeDatePicker(false) },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            dateState.selectedDateMillis?.let {
                                                val selectedDate =
                                                    LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
                                                ordersViewModel.changeDatePicker(false)
                                                ordersViewModel.changeTimePicker(true)
                                            }
                                        }
                                    ) {
                                        Text(text = stringResource(id = R.string.done))
                                    }
                                }
                            ) {
                                DatePicker(state = dateState)
                            }
                        }
                    }

                    if (ordersUiState.isTimePickerVisible) {
                        val initialHour = ordersUiState.selectedTime?.first ?: 0
                        val initialMinute = ordersUiState.selectedTime?.second ?: 0

                        item {
                            TimePickerDialog(
                                LocalContext.current,
                                { _, hour, minute ->
                                    dateState.selectedDateMillis?.let {
                                        val selectedDate =
                                            LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
                                        orderRequest = orderRequest.copy(
                                            timeOfSending = LocalDateTime.of(
                                                selectedDate,
                                                java.time.LocalTime.of(hour, minute)
                                            )
                                        )
                                    }
                                    ordersViewModel.changeTimePicker(false)
                                    ordersViewModel.changeDatePicker(false)
                                },
                                initialHour,
                                initialMinute,
                                true
                            ).show()
                        }
                    }

                    if (ordersUiState.isAddressBottomSheetExpanded) {
                        item {
                            AddressBottomSheet(
                                bottomSheetTitle = stringResource(id = R.string.orders_change_shipment_address),
                                onDismissRequest = {
                                    ordersViewModel.changeAddressBottomSheetStatus(false)
                                },
                                address = orderRequest.address ?: AddressCreateRequest(),
                                onDone = { request ->
                                    ordersViewModel.updateAddress(request)
                                    orderRequest = orderRequest.copy(address = request)
                                }
                            )
                        }
                    }

                    if (ordersUiState.isQuantityBottomSheetExpanded) {
                        item {
                            QuantityBottomSheet(
                                onDismiss = {
                                    ordersViewModel.changeQuantityBottomSheet(false)
                                    currentProductVariant = null
                                   },
                                onConfirm = { quantity ->
                                    currentProductVariant?.let {
                                        productVariantList = productVariantList.toMutableList().apply {
                                            add(
                                                OrdersViewModel.ProductVariantOrderItem(
                                                    productVariant = it,
                                                    quantity = quantity,
                                                    totalPrice = it.variantPrice * BigDecimal(quantity)
                                                )
                                            )
                                        }
                                        ordersViewModel.changeQuantityBottomSheet(false)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}