package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.amoz.R
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductNameDescriptionPrice(
    productName: String,
    productDescription: String,
    productPrice: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        // -------------------- Product's name --------------------
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(stringResource(R.string.product_name)) },
            value = productName,
            onValueChange = { onNameChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            maxLines = 1,
            singleLine = true
        )

        // -------------------- Product's description --------------------
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            value = productDescription,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(stringResource(R.string.product_description)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )

        // -------------------- Product's price --------------------
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = productPrice,
            onValueChange = {
                val priceRegex = "^\\d*(\\.\\d{0,2})?\$".toRegex()
                if (it.matches(priceRegex)) {
                    onPriceChange(it)
                }
            },
            placeholder = { Text("0") },
            label = { Text(stringResource(R.string.product_price)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.CreditCard,
                    contentDescription = null,
                )
            },
            trailingIcon = {
                Text(text = stringResource(R.string.currency))
            },
            maxLines = 1,
            singleLine = true
        )
    }
}