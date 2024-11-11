package com.example.amoz.ui.screens.bottom_screens.company.employees

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.data.Employee
import com.example.amoz.ui.commonly_used_components.SwipeableItemWithActions
import java.time.LocalDate

@Composable
fun EmployeesLazyColumn(
    employees: List<Employee>,
    callSnackBar: (String, ImageVector?) -> Unit,
    changeEmploymentDate: (Int, LocalDate) -> Unit,
    employeeProfileBottomSheetExpanded: Boolean,
    expandEmployeeProfileBottomSheet: (Boolean) -> Unit,
) {
    var currentEmployee by remember {
        mutableStateOf<Employee?>(null)
    }

    val changeEmploymentDateText = stringResource(id = R.string.company_change_employment_date_successful)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(employees) { employee ->
            SwipeableItemWithActions(
                actions = {
                    SwipeableActionsRaw(
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
                        employee.personPhoto?.let {
                            Image(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                painter = painterResource(id = employee.personPhoto),
                                contentDescription = null)
                        }
                    },
                    headlineContent = { Text(text = employee.firstName + " " + employee.lastName) },
                    supportingContent = { Text(text = employee.email) },
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

