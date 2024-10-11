package com.example.bussiness.screens.drawer_screens.products

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun FeatureRow(
    index: Int,
    featureName: String,
    featurePrice: String,
    onDelete: (Int) -> Unit,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(5.dp))
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(2f),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            value = featureName,
            onValueChange = onNameChange,
            label = { Text("Feature Name") },
            placeholder = { Text("Enter feature name") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            maxLines = 1,
            singleLine = true
        )
        Spacer(modifier = Modifier.width(4.dp))
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .height(30.dp)
                .width(1.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            value = featurePrice,
            onValueChange = { newValue ->
                val sanitizedValue = newValue.trim()
                onPriceChange(sanitizedValue)
            },
            label = { Text("Price") },
            placeholder = { Text("0.0") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            maxLines = 1,
            singleLine = true
        )
        IconButton(
            onClick = { onDelete(index) },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(imageVector = Icons.Outlined.Clear, contentDescription = "Delete feature")
        }
    }
}



@Composable
fun FeatureDialog(showFeatureDialog: MutableState<Boolean>, addFeature: (String, FeatureType) -> Unit) {
    val featureName = remember { mutableStateOf("") }
    val featureType = remember { mutableStateOf(FeatureType.TEXT_FIELD) }

    if (showFeatureDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showFeatureDialog.value = false
            },
            title = { Text("Add New Feature") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = featureName.value,
                        onValueChange = { featureName.value = it },
                        label = { Text("Feature Name") }
                    )
                    // Radio buttons for feature type selection
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = featureType.value == FeatureType.TEXT_FIELD,
                            onClick = { featureType.value = FeatureType.TEXT_FIELD }
                        )
                        Text("Text Field")
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(
                            selected = featureType.value == FeatureType.LIST,
                            onClick = { featureType.value = FeatureType.LIST }
                        )
                        Text("List")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        addFeature(featureName.value, featureType.value)
                        showFeatureDialog.value = false
                    }
                ) { Text("Add") }
            },
            dismissButton = {
                Button(onClick = { showFeatureDialog.value = false }) { Text("Cancel") }
            }
        )
    }
}