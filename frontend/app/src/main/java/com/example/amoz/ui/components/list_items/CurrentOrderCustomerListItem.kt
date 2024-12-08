package com.example.amoz.ui.components.list_items

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.models.CustomerAnyRepresentation

@Composable
fun CurrentOrderCustomerListItem(
    currentCustomer: CustomerAnyRepresentation,
    modifier: Modifier = Modifier,
    onRemoveCustomer: () -> Unit,
) {
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
                onRemoveCustomer()
            },
        headlineContent = {
            Text(
                text = currentCustomer.nameOnInvoice
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
}