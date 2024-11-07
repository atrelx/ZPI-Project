package com.example.bussiness.ui.screens.bottom_screens.company.employees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bussiness.R
import com.example.bussiness.ui.PrimaryFilledButton
import com.example.bussiness.ui.screens.bottom_screens.company.CompanyScreenViewModel
import com.example.bussiness.ui.theme.AmozApplicationTheme

@Composable
fun CompanyEmployeesScreen(
    navController: NavHostController,
    companyViewModel: CompanyScreenViewModel,
    paddingValues: PaddingValues,
    callSnackBar: (String, ImageVector?) -> Unit,
) {
    AmozApplicationTheme {
        val companyUiState by companyViewModel.companyUiState.collectAsState()
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                // -------------------- Employees list --------------------
                EmployeesLazyColumn(
                    employees = companyUiState.companyEmployees,
                    callSnackBar = callSnackBar,
                    employeeProfileBottomSheetExpanded = companyUiState.employeeProfileBottomSheetExpanded,
                    expandEmployeeProfileBottomSheet = {
                        companyViewModel.expandEmployeeProfileBottomSheet(it)
                    },
                    changeEmploymentDate = { employeeID, newDate ->
                        companyViewModel.updateEmploymentDate(employeeID, newDate)
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                // -------------------- Add worker button --------------------
                PrimaryFilledButton(
                    onClick = { companyViewModel.expandAddEmployeeBottomSheet(true) },
                    text = stringResource(id = R.string.company_add_worker_button),
                    leadingIcon = Icons.Outlined.Add
                )
            }
        }

        // -------------------- Add worker bottom sheet --------------------
        if (companyUiState.addEmployeeBottomSheetExpanded) {
            AddEmployeesBottomSheet(
                onDismissRequest = {
                    companyViewModel.expandAddEmployeeBottomSheet(false)
                },
                callSnackBar = callSnackBar,
            )
        }
    }
}

