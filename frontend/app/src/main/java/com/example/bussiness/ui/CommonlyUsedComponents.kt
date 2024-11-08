package com.example.bussiness.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.DoorFront
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material.icons.outlined.MarkunreadMailbox
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.bussiness.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PrimaryFilledButton(
    onClick: () -> Unit,
    text: String,
    leadingIcon: ImageVector? = null,
    leadingIconDescription: String? = null,
    isEnabled: Boolean = true,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        enabled = isEnabled
    ) {
        leadingIcon?.let {
            Icon(imageVector = leadingIcon,
                contentDescription = leadingIconDescription)
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CustomSnackBar(
    leadingIcon: ImageVector?,
    message: String,
) {
    Snackbar(
        modifier = Modifier
            .padding(vertical = 5.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment =  Alignment.CenterVertically
        ) {
            leadingIcon?.let {
                Icon(imageVector = it, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBottomSheet(
    bottomSheetTitle: String = stringResource(id = R.string.address_change_title),
    street: String,
    houseNumber: String,
    apartmentNumber: String,
    city: String,
    postalCode: String,
    additionalInfo: String,
    onDismissRequest: () -> Unit,
    onDone: (String, String, String,
             String, String, String) -> Unit
) {
    var streetState by remember { mutableStateOf(street) }
    var houseNumberState by remember { mutableStateOf(houseNumber) }
    var apartmentNumberState by remember { mutableStateOf(apartmentNumber) }
    var cityState by remember { mutableStateOf(city) }
    var postalCodeState by remember { mutableStateOf(postalCode) }
    var additionalInfoState by remember { mutableStateOf(additionalInfo) }

    val regularValueLength = 255
    val shortValueLength = 10
    val postalCodeValueLength = 5

    val isStreetStateValid by remember { derivedStateOf { streetState.isNotBlank() } }
    val isHouseNumberStateValid by remember { derivedStateOf { houseNumberState.isNotBlank() } }
    val isCityStateValid by remember { derivedStateOf { cityState.isNotBlank() } }
    val isPostalCodeStateValid by remember {
        derivedStateOf {
            postalCodeState.isNotBlank() && postalCodeState.length > 2
        }
    }

    val isAddressFormValid by remember {
        derivedStateOf {
            isStreetStateValid && isHouseNumberStateValid &&
                    isCityStateValid && isPostalCodeStateValid
        }
    }

    val focusRequesters = remember { List(6) { FocusRequester() } }
    val focusManager = LocalFocusManager.current

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
                style = MaterialTheme.typography.headlineMedium
            )
            // -------------------- Street --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[0]),
                label = { Text(text = stringResource(id = R.string.street)) },
                value = streetState,
                onValueChange = {
                    if (it.length <= regularValueLength) {
                        streetState = it
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
                isError = !isStreetStateValid,
                maxLines = 1,
                singleLine = true
            )

            // -------------------- House number --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[1]),
                label = { Text(text = stringResource(id = R.string.house_number)) },
                value = houseNumberState,
                onValueChange = {
                    if (it.length <= shortValueLength) {
                        houseNumberState = it
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
                isError = !isHouseNumberStateValid,
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Apartment number --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[2]),
                label = { Text(text = stringResource(id = R.string.apartment_number)) },
                value = apartmentNumberState,
                onValueChange = {
                    if (it.length <= shortValueLength) {
                        apartmentNumberState = it
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
                value = cityState,
                onValueChange = {
                    if (it.length <= regularValueLength) {
                        cityState = it
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
                isError = !isCityStateValid,
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Postal code --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[4]),
                label = { Text(text = stringResource(id = R.string.postal_code)) },
                value = postalCodeState,
                onValueChange = {
                    if (it.length <= postalCodeValueLength && it.isDigitsOnly()) {
                        postalCodeState = it
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
                isError = !isPostalCodeStateValid,
                singleLine = true
            )

            // -------------------- Additional info --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[5]),
                label = { Text(text = stringResource(id = R.string.additional_info)) },
                value = additionalInfoState,
                onValueChange = {
                    if (additionalInfoState.length < regularValueLength) {
                        additionalInfoState = it
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Done button --------------------
            Spacer(modifier = Modifier.height(5.dp))
            PrimaryFilledButton(
                onClick = {
                    onDone(
                        streetState, houseNumberState, apartmentNumberState, cityState,
                        postalCodeState, additionalInfoState
                    )
                    onDismissRequest()
                },
                isEnabled = isAddressFormValid,
                text = stringResource(id = R.string.done),
                leadingIcon = Icons.Outlined.Done
            )
        }
    }
}

@Composable
fun PersonProfileColumn(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(5.dp))
        .border(
            width = 1.dp,
            brush = SolidColor(MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(5.dp)
        ),
    personPhoto: Int?,
    personFirstName: String,
    personLastName: String,
    personEmail: String,
    personPhoneNumber: String?,
    personSex: String,
    personBirthDate: LocalDate
) {

    val listItemColors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    )
    val clipboardManager = LocalClipboardManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        // -------------------- Full name, profile image --------------------
        ListItem(
            modifier = modifier.then(Modifier
                .clickable {
                    clipboardManager.setText(
                        AnnotatedString("$personFirstName $personLastName")
                    )
                }),
            leadingContent = {
                personPhoto?.let {
                    Image(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        painter = painterResource(id = personPhoto),
                        contentDescription = null
                    )
                } ?: run {
                    Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = null)
                }
            },
            overlineContent = { Text(text = stringResource(id = R.string.profile_full_name)) },
            headlineContent = {
                Text(
                    text = "$personFirstName $personLastName",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            colors = listItemColors
        )

        // -------------------- Email --------------------
        ListItem(
            modifier = modifier.then(Modifier
                .clickable {
                    clipboardManager.setText(
                        AnnotatedString(personEmail)
                    )
                }),
            leadingContent = {
                Icon(imageVector = Icons.Outlined.Mail, contentDescription = null)
            },
            overlineContent = { Text(text = stringResource(id = R.string.profile_email)) },
            headlineContent = { Text(text = personEmail) },
            colors = listItemColors
        )

        // -------------------- Phone number --------------------
        personPhoneNumber?.let { phone ->
            ListItem(
                modifier = modifier.then(Modifier
                    .clickable {
                        clipboardManager.setText(
                            AnnotatedString(phone)
                        )
                    }),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Phone, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(id = R.string.profile_phone_number)) },
                headlineContent = { Text(text = phone) },
                colors = listItemColors
            )
        }

        // -------------------- Sex --------------------
        ListItem(
            modifier = modifier,
            leadingContent = {
                Icon(imageVector = Icons.Outlined.Man, contentDescription = null)
            },
            overlineContent = { Text(text = stringResource(id = R.string.profile_sex)) },
            headlineContent = { Text(text = personSex) },
            colors = listItemColors
        )

        // -------------------- Birth date --------------------
        ListItem(
            modifier = modifier.then(Modifier
                .clickable {
                    clipboardManager.setText(
                        AnnotatedString(personBirthDate.format(
                            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        ))
                    )
                }),
            leadingContent = {
                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
            },
            overlineContent = { Text(text = stringResource(id = R.string.profile_birth_date)) },
            headlineContent = {
                Text(
                    text = personBirthDate.format(
                        DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    )
                )
            },
            colors = listItemColors
        )
    }
}

@Composable
fun BottomSheetNavigationRaw(
    leadingIcon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier
            .clickable(onClick = onClick),
        leadingContent = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
            )
        },
        headlineContent = { Text(text = text) },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    )
}