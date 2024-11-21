package com.example.amoz.ui.commonly_used_components

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
import androidx.compose.material.icons.outlined.DoorFront
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.MarkunreadMailbox
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.amoz.R
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.models.Address
import com.example.amoz.ui.components.PrimaryFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBottomSheet(
    bottomSheetTitle: String = stringResource(id = R.string.address_change_title),
    address: AddressCreateRequest,
    onDismissRequest: () -> Unit,
    onDone: (AddressCreateRequest) -> Unit
) {
//    var streetState by remember { mutableStateOf(address.street) }
    var addressState by remember { mutableStateOf(address) }
//    var houseNumberState by remember { mutableStateOf(address.streetNumber) }
//    var apartmentNumberState by remember { mutableStateOf(address.apartmentNumber ?: "") }
//    var cityState by remember { mutableStateOf(address.city) }
//    var postalCodeState by remember { mutableStateOf(address.postalCode) }
//    var additionalInfoState by remember { mutableStateOf(address.additionalInformation ?: "") }

    val regularValueLength = 255
    val shortValueLength = 10
    val postalCodeValueLength = 5

//    val isStreetStateValid by remember { derivedStateOf { streetState.isNotBlank() } }
//    val isHouseNumberStateValid by remember { derivedStateOf { houseNumberState.isNotBlank() } }
//    val isCityStateValid by remember { derivedStateOf { cityState.isNotBlank() } }
//    val isPostalCodeStateValid by remember {
//        derivedStateOf {
//            postalCodeState.isNotBlank() && postalCodeState.length > 2
//        }
//    }

//    val isAddressFormValid by remember {
//        derivedStateOf {
//            isStreetStateValid &&
//                    isHouseNumberStateValid &&
//                    isCityStateValid && isPostalCodeStateValid
//        }
//    }

    val focusRequesters = remember { List(6) { FocusRequester() } }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = bottomSheetTitle,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            // -------------------- Street --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[0]),
                label = { Text(text = stringResource(id = R.string.street)) },
                value = addressState.street,
                onValueChange = {
                    if (it.length <= regularValueLength) {
                        addressState = addressState.copy(street = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Place, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[1].requestFocus() }
                ),
//                isError = !isStreetStateValid,
                maxLines = 1,
                singleLine = true
            )

            // -------------------- House number --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[1]),
                label = { Text(text = stringResource(id = R.string.house_number)) },
                value = addressState.streetNumber,
                onValueChange = {
                    if (it.length <= shortValueLength) {
                        addressState = addressState.copy(streetNumber = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Home, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[2].requestFocus() }
                ),
//                isError = !isHouseNumberStateValid,
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Apartment number --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[2]),
                label = { Text(text = stringResource(id = R.string.apartment_number)) },
                value = addressState.apartmentNumber ?: "",
                onValueChange = {
                    if (it.length <= shortValueLength) {
                        addressState = addressState.copy(apartmentNumber = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.DoorFront, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[3].requestFocus() }
                ),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- City/town --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[3]),
                label = { Text(text = stringResource(id = R.string.city)) },
                value = addressState.city,
                onValueChange = {
                    if (it.length <= regularValueLength) {
                        addressState = addressState.copy(city = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.LocationCity, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[4].requestFocus() }
                ),
//                isError = !isCityStateValid,
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Postal code --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[4]),
                label = { Text(text = stringResource(id = R.string.postal_code)) },
                value = addressState.postalCode,
                onValueChange = {
                    if (it.length <= postalCodeValueLength && it.isDigitsOnly()) {
                        addressState = addressState.copy(postalCode = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.MarkunreadMailbox, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[5].requestFocus() }
                ),
                maxLines = 1,
//                isError = !isPostalCodeStateValid,
                singleLine = true
            )

            // -------------------- Additional info --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[5]),
                label = { Text(text = stringResource(id = R.string.additional_info)) },
                value = addressState.additionalInformation ?: "",
                onValueChange = {
//                    if (additionalInfoState.length < regularValueLength) {
                    addressState = addressState.copy(additionalInformation = it)
//                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Done button --------------------
            Spacer(modifier = Modifier.height(5.dp))
            PrimaryFilledButton(
                onClick = {
                    onDone(
//                        streetState, houseNumberState, apartmentNumberState, cityState,
//                        postalCodeState, additionalInfoState
                        addressState
                    )
                    onDismissRequest()
                },
//                enabled = isAddressFormValid,
                text = stringResource(id = R.string.done),
                leadingIcon = Icons.Outlined.Done
            )
        }
    }
}