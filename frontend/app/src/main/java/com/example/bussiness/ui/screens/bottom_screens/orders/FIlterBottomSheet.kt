package com.example.bussiness.ui.screens.bottom_screens.orders

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun BottomSheetContent(
    priceFrom: Float,
    priceTo: Float,
    startDate: Long?,
    endDate: Long?,
    onApplyFilters: (Float, Float, Long?, Long?) -> Unit,
    onCancelFilters: () -> Unit
) {
    var sliderPosition by remember { mutableStateOf(priceFrom..priceTo) }
    var localStartDate by remember { mutableStateOf(startDate) }
    var localEndDate by remember { mutableStateOf(endDate) }

    var fromPriceText by remember { mutableStateOf(priceFrom.toInt().toString()) }
    var toPriceText by remember { mutableStateOf(priceTo.toInt().toString()) }

    val context = LocalContext.current

    val fromDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                localStartDate = calendar.timeInMillis
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }

    val toDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                localEndDate = calendar.timeInMillis
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text("Price Range:",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = fromPriceText,
                    onValueChange = {
                        fromPriceText = it
                        val fromValue = it.toFloatOrNull() ?: 0f
                        sliderPosition = fromValue..sliderPosition.endInclusive
                    },
                    label = { Text("Price From") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = toPriceText,
                    onValueChange = {
                        toPriceText = it
                        val toValue = it.toFloatOrNull() ?: 1000f
                        sliderPosition = sliderPosition.start..toValue
                    },
                    label = { Text("Price To") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            RangeSlider(
                value = sliderPosition,
                onValueChange = { range ->
                    sliderPosition = range
                    fromPriceText = range.start.toInt().toString()
                    toPriceText = range.endInclusive.toInt().toString()
                },
                valueRange = 0f..1000f,
                steps = 100,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { fromDatePickerDialog.show() },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "From Date: ${localStartDate?.let { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(it) } ?: "Not set"}")
            }
            OutlinedButton(
                onClick = { toDatePickerDialog.show() },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "To Date: ${localEndDate?.let { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(it) } ?: "Not set"}")
            }
        }

        Column {
            OutlinedButton(
                onClick = {
                    onCancelFilters()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel All Filters")
            }

            Button(
                onClick = {
                    onApplyFilters(
                        sliderPosition.start,
                        sliderPosition.endInclusive,
                        localStartDate,
                        localEndDate
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply Filters")
            }
        }
    }
}
