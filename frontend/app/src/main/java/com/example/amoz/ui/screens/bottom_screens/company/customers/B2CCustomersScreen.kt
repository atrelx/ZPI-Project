package com.example.amoz.ui.screens.bottom_screens.company.customers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.amoz.data.Person
import com.example.amoz.ui.PersonProfileColumn
import com.example.amoz.view_models.CompanyViewModel
import com.example.amoz.ui.theme.AmozApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun B2CCustomerScreen(
    b2cCustomersList: List<Person>,
    companyViewModel: CompanyViewModel,
    callSnackBar: (String, ImageVector?) -> Unit,
    ) {
    AmozApplicationTheme {
        val companyUiState by companyViewModel.companyUiState.collectAsState()
        var currentB2cCustomer by remember { mutableStateOf<Person?>(null) }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            // -------------------- B2C Customers --------------------
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(b2cCustomersList.reversed()) { person ->
                    ListItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                currentB2cCustomer = person
                                companyViewModel.expandCustomerProfileDataBottomSheet(true)
                            },
                        headlineContent = { Text(person.firstName + " " + person.lastName) },
                        supportingContent = { Text(text = person.email) },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    )
                }
            }
            // -------------------- FAB --------------------
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        companyViewModel.expandAddB2CCustomerBottomSheet(true)
                    },
                    modifier = Modifier
                        .padding(16.dp), // Padding for spacing from screen edges
                    icon = { Icon(Icons.Filled.Add, null) },
                    text = { Text(text = "Add customer") }
                )
            }
        }
        if (companyUiState.addB2CCustomerBottomSheetExpanded) {
            AddB2CCustomerBottomSheet(
                onDismissRequest = {
                    companyViewModel.expandAddB2CCustomerBottomSheet(false)
                },
                callSnackBar = callSnackBar,
                addB2CCustomer = { firstName, lastName, email, phone ->
                    companyViewModel.addB2CCustomer(firstName, lastName, email, phone)
                }

            )
        }
        if (companyUiState.customerProfileDataBottomSheetExpanded && currentB2cCustomer != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    currentB2cCustomer = null
                    companyViewModel.expandAddB2CCustomerBottomSheet(false)
                },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                ) {
                Column (
                    modifier = Modifier
                        .padding(16.dp),
                ) {
                    currentB2cCustomer?.let { currentCustomer ->
                        PersonProfileColumn(
                            personPhoto = null,
                            personFirstName = currentCustomer.firstName,
                            personLastName = currentCustomer.lastName,
                            personEmail = currentCustomer.email,
                            personPhoneNumber = currentCustomer.phoneNumber,
                            personSex = currentCustomer.sex,
                            personBirthDate = currentCustomer.dateOfBirth
                        )
                    }
                }
            }
        }
    }
}

