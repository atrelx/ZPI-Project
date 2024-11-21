package com.example.amoz.ui.screens.bottom_screens.company.employees

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.models.Employee
import com.example.amoz.ui.commonly_used_components.ResultStateView
import com.example.amoz.ui.commonly_used_components.SwipeableItemWithActions
import com.example.amoz.ui.commonly_used_components.loadImageBitmapFromResource
import com.example.amoz.ui.screens.bottom_screens.company.CompanyViewModel
import java.time.LocalDate
import java.util.UUID

@Composable
fun EmployeesLazyColumn(
    employees: List<Employee>,
    companyViewModel: CompanyViewModel,
    callSnackBar: (String, ImageVector?) -> Unit,
    changeEmploymentDate: (UUID, LocalDate) -> Unit,
    employeeProfileBottomSheetExpanded: Boolean,
    expandEmployeeProfileBottomSheet: (Boolean) -> Unit,
) {
    var currentEmployee by remember {
        mutableStateOf<Employee?>(null)
    }

    val changeEmploymentDateText = stringResource(id = R.string.company_change_employment_date_successful)

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        state = listState
    ) {
        items(employees) { employee ->
            val isEmployeeVisible = remember {
                derivedStateOf {
                    val visibleItems = listState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.index == employees.indexOf(employee) }
                }
            }

            LaunchedEffect(isEmployeeVisible.value) {
                if (isEmployeeVisible.value) {
                    companyViewModel.getProfilePicture(employee.employeeId)
                }
            }

            SwipeableItemWithActions(
                actions = {
                    EmployeeActionsRaw(
                        employee = employee,
                        onEmployeeProfileClick = {
                            currentEmployee = employee
                            expandEmployeeProfileBottomSheet(true)
                        },
                        onEmployeeDelete = {/*TODO*/}
                    )
                }
            ) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    leadingContent = {
                        val state = companyViewModel.companyUIState
                            .collectAsState().value.employeeImages
                            .collectAsState().value[employee.employeeId]
                        state?.let {
                            ResultStateView(state,
                                modifier = Modifier.size(56.dp),
                                successView =  { image ->
                                    Image(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(10.dp)),
                                        bitmap = image ?: loadImageBitmapFromResource(R.drawable.human_placeholder),
                                        contentDescription = null
                                    )
                                },
                                failureView = {
                                    Image(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(10.dp)),
                                        bitmap =  loadImageBitmapFromResource(R.drawable.human_placeholder),
                                        contentDescription = null
                                    )
                                })

                        }
                    },
                    headlineContent = { Text(text = employee.person.name + " " + employee.person.surname) },
                    supportingContent = { Text(text = employee.contactPerson.emailAddress ?: "No email address") },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                )
            }
        }
    }
    if (employeeProfileBottomSheetExpanded && currentEmployee != null) {
        EmployeeProfileBottomSheet(
            onDismissRequest = {
                currentEmployee = null
                expandEmployeeProfileBottomSheet(false)
            },
            employee = currentEmployee!!,
            onDone = { employeeID, newDate ->
                changeEmploymentDate(employeeID, newDate)
                callSnackBar(changeEmploymentDateText, Icons.Outlined.Done)
            }
        )
    }
}

