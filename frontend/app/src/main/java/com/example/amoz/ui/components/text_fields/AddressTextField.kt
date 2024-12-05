package com.example.amoz.ui.components.text_fields

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.amoz.R
import com.example.amoz.api.requests.AddressCreateRequest


@Composable
fun AddressTextField(
    modifier: Modifier = Modifier,
    address: AddressCreateRequest? = null,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit,
) {
    fun addressToText(address: AddressCreateRequest): String {
        return "${address.street} ${address.streetNumber} ${address.apartmentNumber ?: ""} ${address.postalCode} ${address.city}"
    }

   OutlinedTextField(
       modifier = modifier
           .fillMaxWidth(),
       value = address?.let { addressToText(it) } ?: stringResource(R.string.orders_address),
       label = { Text(stringResource(R.string.orders_address)) },
       trailingIcon = {
              trailingIcon?.let {
                 Icon(it, contentDescription = null, Modifier.clickable(onClick = onClick))
              }},
       textStyle = MaterialTheme.typography.bodyLarge,
       onValueChange = {},
       readOnly = true,
       maxLines = 1,
       singleLine = true,
   )


}
