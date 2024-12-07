package com.example.amoz.ui.components.dropdown_menus

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.amoz.R
import com.example.amoz.api.enums.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusDropdownMenu(
    selectedStatus: Status,
    onStatusChange: (Status) -> Unit,
    modifier: Modifier = Modifier
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = dropDownMenuExpanded,
        onExpandedChange = { dropDownMenuExpanded = !dropDownMenuExpanded }
    ) {
        OutlinedTextField(
            value = selectedStatus.getName(),
            onValueChange = { },
            label = { Text(stringResource(R.string.orders_select_status)) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = if (dropDownMenuExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        ExposedDropdownMenu(
            expanded = dropDownMenuExpanded,
            onDismissRequest = { dropDownMenuExpanded = false }
        ) {
            Status.values().forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.getName()) },
                    onClick = {
                        onStatusChange(status)
                        dropDownMenuExpanded = false
                    }
                )
            }
        }
    }
}