package com.example.amoz.ui.components.filters

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.amoz.R

@Composable
fun PriceFilter(
    priceFrom: String,
    priceTo: String,
    onPriceFromChange: (String) -> Unit,
    onPriceToChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = priceFrom,
            onValueChange = {
                onPriceFromChange(it)
            },
            label = { Text(stringResource(id = R.string.price_from)) },
            placeholder = { Text("0") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(10.dp))
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = priceTo,
            onValueChange = {
                onPriceToChange(it)
            },
            label = { Text(stringResource(id = R.string.price_to)) },
            placeholder = { Text("-") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )
    }
}