package com.example.bussiness.ui.screens.bottom_screens.company.customers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bussiness.R
import com.example.bussiness.ui.PrimaryFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddB2BCustomerBottomSheet(
    onDismissRequest: () -> Unit,
    callSnackBar: (String, ImageVector?) -> Unit,
    addB2BCustomer: (String, String, String, String) -> Unit
) {
    var companyName by remember { mutableStateOf("") }
    var companyEmail by remember { mutableStateOf("") }
    var companyAddress by remember { mutableStateOf("") }
    var companyIdentifier by remember { mutableStateOf("") }

    val emailPattern = stringResource(id = R.string.email_pattern)

    val isFormValid by remember {
        derivedStateOf {
            companyName.isNotBlank() &&
            companyAddress.isNotBlank() &&
            companyIdentifier.isNotBlank() &&
            companyEmail.matches(emailPattern.toRegex())

        }
    }

    val invSentSuccessful = stringResource(id = R.string.company_add_customer_successful)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // -------------------- Title --------------------
            Text(
                text = stringResource(id = R.string.company_customers_add_customer),
                style = MaterialTheme.typography.headlineMedium
            )

            // -------------------- Company name --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.company_name))
                },
                value = companyName,
                onValueChange = {
                    companyName = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.DriveFileRenameOutline,
                        contentDescription = null
                    )
                },
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Company email --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.profile_email))
                },
                value = companyEmail,
                onValueChange = {
                    companyEmail = it
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Mail, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Company Address --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.company_address))
                },
                value = companyAddress,
                onValueChange = {
                    companyAddress = it
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Place, contentDescription = null)
                },
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Company number --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.company_number))
                },
                value = companyIdentifier,
                onValueChange = {
                    companyIdentifier = it
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Numbers, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Confirm button --------------------
            PrimaryFilledButton(
                onClick = {
                    /*TODO: Change addB2CCustomer func*/
                    addB2BCustomer(
                        companyName,
                        companyAddress,
                        companyEmail,
                        companyIdentifier
                    )
                    onDismissRequest()
                    callSnackBar(
                        invSentSuccessful,
                        Icons.Outlined.Done
                    )
                },
                isEnabled = isFormValid,
                text = stringResource(id = R.string.done)
            )
        }
    }
}