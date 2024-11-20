package com.example.amoz.ui.screens.bottom_screens.company

import androidx.compose.ui.graphics.ImageBitmap
import com.example.amoz.data.Person
import com.example.amoz.R
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.CustomerB2B
import com.example.amoz.models.Company
import com.example.amoz.models.CustomerB2C
//import com.example.amoz.data.Employee
import com.example.amoz.models.Employee
import com.example.amoz.ui.screens.bottom_screens.company.customers.testB2BCustomers
import com.example.amoz.ui.screens.bottom_screens.company.customers.testB2СCustomers
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

data class CompanyScreenUIState (
    val companyLogo: Int = R.drawable.pizzeria,

    val company: MutableStateFlow<ResultState<Company>> = MutableStateFlow(ResultState.Idle),
    val employees: MutableStateFlow<ResultState<List<Employee>>> = MutableStateFlow(ResultState.Idle),
    val employeeImages: MutableStateFlow<MutableMap<UUID, MutableStateFlow<ResultState<ImageBitmap?>>>> = MutableStateFlow(HashMap()),
    val companyB2BCustomers: MutableStateFlow<ResultState<MutableList<CustomerB2B>>> = MutableStateFlow(ResultState.Idle),
    val companyB2CCustomers: MutableStateFlow<ResultState<MutableList<CustomerB2C>>> = MutableStateFlow(ResultState.Idle),

    val changeCompanyNameBottomSheetExpanded: Boolean = false,
    val changeCompanyAddressBottomSheetExpanded: Boolean = false,
    val addEmployeeBottomSheetExpanded: Boolean = false,
    val employeeProfileBottomSheetExpanded: Boolean = false,
    val addB2CCustomerBottomSheetExpanded: Boolean = false,
    val addB2BCustomerBottomSheetExpanded: Boolean = false,
    val customerProfileDataBottomSheetExpanded: Boolean = false,
)