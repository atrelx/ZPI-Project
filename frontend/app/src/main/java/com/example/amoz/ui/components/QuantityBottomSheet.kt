package com.example.amoz.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.amoz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
    ) {
        var quantity by remember { mutableStateOf(1) }

        BottomSheetScaffold(
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.enter_quantity),
                        style = MaterialTheme.typography.displaySmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = quantity.toString(),
                        onValueChange = { newValue ->
                            quantity = newValue.toIntOrNull() ?: 1
                        },
                        label = { Text(stringResource(R.string.quantity)) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onConfirm(quantity) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.confirm))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            },
            sheetPeekHeight = 0.dp,
            content = {
                // Content can be left empty or used for other UI elements in the screen
            }
        )
}