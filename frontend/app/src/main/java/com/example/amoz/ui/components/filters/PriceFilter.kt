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
import com.example.amoz.ui.components.PriceTextField
import java.math.BigDecimal

@Composable
fun PriceFilter(
    priceFrom: BigDecimal?,
    priceTo: BigDecimal?,
    onPriceFromChange: (BigDecimal) -> Unit,
    onPriceToChange: (BigDecimal) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        PriceTextField(
            modifier = Modifier.weight(1f),
            label = stringResource(id = R.string.price_from),
            price = priceFrom,
            onPriceChange = { onPriceFromChange(it) }
        )
        Spacer(modifier = Modifier.width(10.dp))
        PriceTextField(
            modifier = Modifier.weight(1f),
            label = stringResource(id = R.string.price_to),
            price = priceTo,
            onPriceChange = { onPriceToChange(it) }
        )

    }
}