package com.example.amoz.ui.components.pickers

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.models.CustomerAnyRepresentation
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.pickers.CustomerPicker

@Composable
fun CustomerPickerListItem(
    currentCustomer: CustomerAnyRepresentation?,
    modifier: Modifier = Modifier,
    onCustomerChange: (CustomerAnyRepresentation) -> Unit,
    onSaveState: () -> Unit,
    onRemoveCustomer: () -> Unit,
    navController: NavController
) {
    val customerPicker = CustomerPicker(navController)
    val selectedCustomer = customerPicker.getPickedCustomer()

    LaunchedEffect(selectedCustomer) {
        Log.d("CustomerPickerListItem", "selectedCustomer: $selectedCustomer")
        Log.d("CustomerPickerListItem", "isCustomerSelected: $currentCustomer")
        if (selectedCustomer != null) {
            onCustomerChange(selectedCustomer)
        }
    }

    if (currentCustomer != null) {
        ListItem(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 1.dp,
                    brush = SolidColor(MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(5.dp)
                )
                .clickable {
                    customerPicker.removePickedCustomer()
                    onRemoveCustomer()
                },
            headlineContent = {
                Text(
                    text = currentCustomer.nameOnInvoice
                        ?: stringResource(R.string.choose_a_customer)
                )
            },
            supportingContent = {
                Text(
                    text = currentCustomer.emailAddress ?: currentCustomer.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_a_customer),
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        )
    } else {
        Row (
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    onSaveState()
                    customerPicker.navigateToCustomerScreen()
                })
        ){
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.choose_a_customer),
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.choose_a_customer),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}