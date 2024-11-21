package com.example.amoz.ui.screens.bottom_screens.company.customers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amoz.models.CustomerB2B
import com.example.amoz.view_models.CompanyViewModel
import com.example.amoz.ui.theme.AmozApplicationTheme

@Composable
fun B2BCustomerScreen(
    b2bCustomersList: List<CustomerB2B>,
    companyViewModel: CompanyViewModel = hiltViewModel(),
    callSnackBar: (String, ImageVector?) -> Unit,
) {
    AmozApplicationTheme {
        val companyUiState by companyViewModel.companyUIState.collectAsState()

        var currentB2bCustomer by remember { mutableStateOf<CustomerB2B?>(null) }
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
                items(b2bCustomersList) { customerB2B ->
                    ListItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                currentB2bCustomer = customerB2B
                                companyViewModel.expandCustomerProfileDataBottomSheet(true)
                            },
                        headlineContent = { Text(customerB2B.nameOnInvoice) },
                        supportingContent = { Text(text = customerB2B.address.fullAddress) },
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
                        companyViewModel.expandAddB2BCustomerBottomSheet(true)
                    },
                    modifier = Modifier
                        .padding(16.dp),
                    icon = { Icon(Icons.Filled.Add, null) },
                    text = { Text(text = "Add customer") }
                )
            }
        }
        if (companyUiState.addB2BCustomerBottomSheetExpanded) {
            AddB2BCustomerBottomSheet(
                onDismissRequest = {
                    companyViewModel.expandAddB2BCustomerBottomSheet(false)
                },
                callSnackBar = callSnackBar,
                customer = companyViewModel.customerB2BCreateRequestState.collectAsState().value,
                onDone = { request ->
                    companyViewModel.createB2BCustomer(request)
                }

            )
        }
        if (companyUiState.customerProfileDataBottomSheetExpanded && currentB2bCustomer != null) {
            currentB2bCustomer?.let {
                B2BCustomerProfileDataBottomSheet(
                    onDismissRequest = {
                        currentB2bCustomer = null
                        companyViewModel.expandCustomerProfileDataBottomSheet(false)
                    },
                    b2BCustomer = it
                )
            }
        }
    }
}

