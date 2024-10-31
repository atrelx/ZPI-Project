package com.example.bussiness.ui.screens.bottom_screens.company

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bussiness.R
import com.example.bussiness.ui.theme.AmozApplicationTheme
import kotlinx.coroutines.launch

@Composable
fun CompanyAddressScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    companyViewModel: CompanyScreenViewModel,
    callSnackBar: (String, ImageVector) -> Unit,
) {
    AmozApplicationTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            val companyUiState by companyViewModel.companyUiState.collectAsState()

            val addressLabels = listOf(
                stringResource(id = R.string.street),
                stringResource(id = R.string.house_number),
                stringResource(id = R.string.city),
                stringResource(id = R.string.postal_code),
            )
            val addressSplited = companyUiState.companyAddress.split(",")
            val addressEditableItems = remember { addressSplited.map { mutableStateOf(it) } }

            val focusRequesters = remember { List(addressSplited.size) { FocusRequester() } }
            val focusManager = LocalFocusManager.current

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // -------------------- Address elements --------------------
                addressEditableItems.zip(addressLabels).forEachIndexed { index, (addressItemEditable, addressLabel) ->
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequesters[index]),
                        label = { Text(text = addressLabel) },
                        value = addressItemEditable.value,
                        onValueChange = { newText ->
                            addressItemEditable.value = newText
                        },
                        trailingIcon = {
                            Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = if (index < addressEditableItems.size - 1) ImeAction.Next else ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusRequesters.getOrNull(index + 1)?.requestFocus() },
                            onDone = { focusManager.clearFocus() }
                        ),
                        maxLines = 1,
                        singleLine = true
                    )
                }
                // -------------------- Done button --------------------
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    onClick = { companyViewModel.updateCompanyAddress(
                        addressEditableItems.joinToString(separator = ", ") { it.value }
                    )
                    navController.navigateUp()
                    callSnackBar("Company address changed", Icons.Filled.Done)
                    }
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
            }
        }
    }
}