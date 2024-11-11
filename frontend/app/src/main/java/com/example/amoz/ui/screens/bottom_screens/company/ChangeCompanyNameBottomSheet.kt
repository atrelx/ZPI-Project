package com.example.amoz.ui.screens.bottom_screens.company

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.ui.commonly_used_components.PrimaryFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeCompanyNameBottomSheet(
    companyName: String,
    onDismissRequest: () -> Unit,
    onDone: (String) -> Unit
) {
    val companyNameValueLength = 30
    var companyNameState by remember { mutableStateOf(companyName)}
    val isCompanyNameStateValid by remember { derivedStateOf{
        companyNameState.isNotBlank() && companyNameState.length < companyNameValueLength }
    }

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.company_name)) },
                value = companyNameState,
                onValueChange = {
                    if (it.length <= companyNameValueLength) {
                        companyNameState = it
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.DriveFileRenameOutline, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isCompanyNameStateValid) {
                            onDismissRequest()
                            onDone(companyNameState)
                        }
                    }
                ),
                isError = !isCompanyNameStateValid,
                maxLines = 1,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(5.dp))
            PrimaryFilledButton(
                onClick = {
                    onDone(companyNameState)
                    onDismissRequest()
                },
                enabled = isCompanyNameStateValid,
                text = stringResource(id = R.string.done),
                leadingIcon = Icons.Outlined.Done
            )
        }
    }
}