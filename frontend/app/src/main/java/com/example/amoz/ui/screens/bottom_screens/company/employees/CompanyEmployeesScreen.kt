package com.example.amoz.ui.screens.bottom_screens.company.employees

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.amoz.R
import com.example.amoz.api.enums.RoleInCompany
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.view_models.CompanyViewModel
import com.example.amoz.ui.theme.AmozApplicationTheme

@Composable
fun CompanyEmployeesScreen(
    navController: NavHostController,
    companyViewModel: CompanyViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    callSnackBar: (String, ImageVector?) -> Unit,
) {
    AmozApplicationTheme {
        val companyUIState by companyViewModel.companyUIState.collectAsState()
        val employeeImages by companyUIState.employeeImages.collectAsState()
        val currentEmployeeRoleInCompany = companyUIState.currentEmployee!!.roleInCompany
        val isCurrentEmployeeOwner = currentEmployeeRoleInCompany == RoleInCompany.OWNER

        val navBackStackEntry = remember { navController.currentBackStackEntryFlow }
        LaunchedEffect(navBackStackEntry) {
            companyViewModel.fetchEmployees()
        }

        ResultStateView(companyUIState.employees) { employees ->
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
                        employees = employees,
                        employeeImages = employeeImages,
                        currentEmployeeRoleInCompany = currentEmployeeRoleInCompany,
                        employeeProfileBottomSheetExpanded = companyUIState.employeeProfileBottomSheetExpanded,
                        getProfilePicture = companyViewModel::getProfilePicture,
                        expandEmployeeProfileBottomSheet = {
                            companyViewModel.expandEmployeeProfileBottomSheet(it)
                        },
                        changeEmploymentDate = { employeeID, newDate ->
//                            companyViewModel.updateEmploymentDate(employeeID, newDate)
                        },
                        callSnackBar = callSnackBar,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    // -------------------- Add worker button --------------------
                    if(isCurrentEmployeeOwner) {
                        PrimaryFilledButton(
                            onClick = { companyViewModel.expandAddEmployeeBottomSheet(true) },
                            text = stringResource(id = R.string.company_add_worker_button),
                            leadingIcon = Icons.Outlined.Add
                        )
                    }
                }
            }

            // -------------------- Add worker bottom sheet --------------------
            if (companyUIState.addEmployeeBottomSheetExpanded) {
                AddEmployeesBottomSheet(
                    onInvitationSend = { companyViewModel.inviteNewEmployee(it, currentEmployeeRoleInCompany) },
                    onDismissRequest = {
                        companyViewModel.expandAddEmployeeBottomSheet(false)
                    },
                    callSnackBar = callSnackBar,
                )
            }
        }
    }
}

