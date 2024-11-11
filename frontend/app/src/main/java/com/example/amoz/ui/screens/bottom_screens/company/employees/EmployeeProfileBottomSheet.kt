package com.example.amoz.ui.screens.bottom_screens.company.employees

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.data.Employee
import com.example.amoz.ui.PersonProfileColumn
import com.example.amoz.ui.commonly_used_components.PrimaryFilledButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeProfileBottomSheet(
    onDismissRequest: () -> Unit,
    onDone: (Int, LocalDate) -> Unit,
    employee: Employee,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        var employmentDate by remember { mutableStateOf(employee.employmentDate) }
        val dateState = rememberDatePickerState()
        var isDatePickerVisible by remember { mutableStateOf(false)}

        val listItemColors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            val listItemModifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 1.dp,
                    brush = SolidColor(MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(5.dp)
                )

            // -------------------- Person profile data --------------------
            PersonProfileColumn(
                personPhoto = employee.personPhoto,
                personFirstName = employee.firstName,
                personLastName = employee.lastName,
                personEmail = employee.email,
                personPhoneNumber = employee.phoneNumber,
                personSex = employee.sex,
                personBirthDate = employee.dateOfBirth
            )

            HorizontalDivider()

            // -------------------- Employment date --------------------
            ListItem(
                modifier = listItemModifier.then(Modifier
                    .clickable {
                        isDatePickerVisible = true
                    } ),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.CalendarToday, contentDescription = null)
                },
                overlineContent = {
                    Text(text = stringResource(id = R.string.profile_employment_date))
                },
                headlineContent = { Text(text = employmentDate.format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                )) },
                trailingContent = {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                },
                colors = listItemColors
            )

            // -------------------- Role in company --------------------
            ListItem(
                modifier = listItemModifier,
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Business, contentDescription = null)
                },
                overlineContent = {Text(text = stringResource(id = R.string.company_employee_role))},
                headlineContent = { Text(text = "Employee") }, //TODO
                colors = listItemColors
            )

            // -------------------- Done button --------------------
            if (employmentDate != employee.employmentDate) {
                PrimaryFilledButton(
                    onClick = {
                        onDone(employee.employeeId!!, employmentDate)
                        onDismissRequest()
                    },
                    leadingIcon = Icons.Outlined.Done,
                    text = stringResource(id = R.string.done)
                )
            }
        }
        if (isDatePickerVisible) {
            DatePickerDialog(
                onDismissRequest = { isDatePickerVisible = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dateState.selectedDateMillis?.let {
                                employmentDate = LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
                            }
                            isDatePickerVisible = false
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

