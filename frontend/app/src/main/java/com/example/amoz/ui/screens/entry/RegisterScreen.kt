package com.example.amoz.ui.screens.entry

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.amoz.R
import com.example.amoz.api.enums.Sex
import com.example.amoz.api.requests.ContactPersonCreateRequest
import com.example.amoz.api.requests.PersonCreateRequest
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen (
    navController: NavHostController,
    paddingValues: PaddingValues,
    userViewModel: UserViewModel = hiltViewModel(),
){
    val userUiState by userViewModel.userUiState.collectAsState()

    var personData = remember {
        PersonCreateRequest(
            name = "",
            surname = "",
            sex = Sex.M,
            dateOfBirth = LocalDate.of(1990, 1, 1),
        )
    }

    var contactPersonData = remember {
        ContactPersonCreateRequest(
            contactNumber = "",
            emailAddress = "",
        )
    }

    var birthDate by remember { mutableStateOf(personData.dateOfBirth) }
    val dateState = rememberDatePickerState()
    var validationMessage by remember { mutableStateOf<String?>(null) }


    AmozApplicationTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.entry_almost_ready),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedTextField(
                    value = personData.name,
                    onValueChange = { personData = personData.copy(name = it) },
                    label = { Text(stringResource(R.string.profile_first_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge
                )

                OutlinedTextField(
                    value = personData.surname,
                    onValueChange = { personData = personData.copy(surname = it) },
                    label = { Text(stringResource(R.string.profile_last_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge
                )

                ExposedDropdownMenuBox(
                    expanded = userUiState.isRegisterDropDownExpanded,
                    onExpandedChange = { userViewModel.changeRegisterDropDownExpanded(it) }
                ) {
                    OutlinedTextField(
                        value = personData.sex.getName(),
                        onValueChange = { },
                        label = { Text(stringResource(R.string.profile_sex)) },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = if (userUiState.isRegisterDropDownExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyLarge
                    )

                    ExposedDropdownMenu(
                        expanded = userUiState.isRegisterDropDownExpanded,
                        onDismissRequest = { userViewModel.changeRegisterDropDownExpanded(false) }
                    ) {
                        Sex.values().forEach { sex ->
                            DropdownMenuItem(
                                text = { Text(sex.getName()) },
                                onClick = {
                                    personData = personData.copy(sex = sex)
                                    userViewModel.changeRegisterDropDownExpanded(false)
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = birthDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    onValueChange = { },
                    label = { Text(stringResource(R.string.profile_birth_date)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            modifier = Modifier.clickable { userViewModel.changeRegisterDatePickerVisible(true) }
                        )
                    },
                    readOnly = true
                )

                OutlinedTextField(
                    value = contactPersonData.emailAddress ?: "",
                    onValueChange = { contactPersonData = contactPersonData.copy(emailAddress = it) },
                    label = { Text(stringResource(R.string.profile_email)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge
                )

                OutlinedTextField(
                    value = contactPersonData.contactNumber,
                    onValueChange = { contactPersonData = contactPersonData.copy(contactNumber = it) },
                    label = { Text(stringResource(R.string.profile_phone_number)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge
                )

                PrimaryFilledButton(
                    onClick = {
                        val personViolations = personData.validate()
                        val contactPersonViolations = contactPersonData.validate()

                        userViewModel.updateCurrentUserRegisterRequest(
                            personData,
                            contactPersonData
                        )


                        navController.navigate(Screens.RegisterImage.route)

                        if (personViolations != null) {
                            validationMessage += "\n" + personViolations
                        } else if (contactPersonViolations != null) {
                            validationMessage += "\n" + contactPersonViolations
                        } else {
                            userViewModel.updateCurrentUserRegisterRequest(
                                personData,
                                contactPersonData
                            )

                            navController.navigate(Screens.RegisterImage.route)
                        }
                    },
                    text = stringResource(R.string.entry_continue),
                )

                if (userUiState.isRegisterDatePickerVisible) {
                    DatePickerDialog(
                        onDismissRequest = { userViewModel.changeRegisterDatePickerVisible(false) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    dateState.selectedDateMillis?.let {
                                        birthDate = LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
                                    }
                                    userViewModel.changeRegisterDatePickerVisible(false)
                                }
                            ) {
                                Text(text = stringResource(id = R.string.done))
                            }
                        }
                    ) {
                        DatePicker(state = dateState)
                    }
                }
            }
        }

        validationMessage?.let {
            Log.d("ValidationMessageRegisterScreen", it)

            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }


    }
}

