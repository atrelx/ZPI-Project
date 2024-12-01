package com.example.amoz.ui.screens.bottom_screens.company.employees

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amoz.R
import com.example.amoz.api.enums.RoleInCompany
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.Employee
import com.example.amoz.ui.components.person.PersonImage
import com.example.amoz.ui.components.SwipeableItemWithActions
import com.example.amoz.view_models.CompanyViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.util.UUID

@Composable
fun EmployeesLazyColumn(
    employees: List<Employee>,
    employeeImages: MutableMap<UUID, MutableStateFlow<ResultState<ImageBitmap?>>>,
    currentEmployeeRoleInCompany: RoleInCompany,
    employeeProfileBottomSheetExpanded: Boolean,
    getProfilePicture: (UUID) -> Unit,
    expandEmployeeProfileBottomSheet: (Boolean) -> Unit,
    onEmployeeDelete: (UUID) -> Unit,
    callSnackBar: (String, ImageVector?) -> Unit,
) {
    val isCurrentEmployeeOwner = currentEmployeeRoleInCompany == RoleInCompany.OWNER

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
        items(employees, key = {it.employeeId}) { employee ->
            val isEmployeeVisible = remember {
                derivedStateOf {
                    val visibleItems = listState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.index == employees.indexOf(employee) }
                }
            }

            LaunchedEffect(isEmployeeVisible.value) {
                if (isEmployeeVisible.value) {
                    getProfilePicture(employee.employeeId)
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
                        onEmployeeDelete = if (isCurrentEmployeeOwner) {{
                            onEmployeeDelete(employee.employeeId)
                        }} else null
                    )
                }
            ) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    leadingContent = { PersonImage(employeeImages[employee.employeeId]) },
                    headlineContent = { Text(text = employee.person.name + " " + employee.person.surname) },
                    supportingContent = { Text(text = employee.contactPerson.emailAddress ?: employee.contactPerson.contactNumber) },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                )
            }
        }
    }
    if (employeeProfileBottomSheetExpanded && currentEmployee != null) {
        EmployeeProfileBottomSheet(
            employee = currentEmployee!!,
            employeeProfileImage = employeeImages[currentEmployee!!.employeeId],
            currentEmployeeRoleInCompany = currentEmployeeRoleInCompany,
            onDismissRequest = {
                currentEmployee = null
                expandEmployeeProfileBottomSheet(false)
            }
        )
    }
}

