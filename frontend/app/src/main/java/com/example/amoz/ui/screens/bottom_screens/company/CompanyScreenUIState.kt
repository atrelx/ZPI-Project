package com.example.amoz.ui.screens.bottom_screens.company

import androidx.compose.ui.graphics.ImageBitmap
import com.example.amoz.data.Person
import com.example.amoz.R
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.data.B2BCustomer
import com.example.amoz.models.Company
//import com.example.amoz.data.Employee
import com.example.amoz.models.Employee
import com.example.amoz.ui.screens.bottom_screens.company.customers.testB2BCustomers
import com.example.amoz.ui.screens.bottom_screens.company.customers.testB2СCustomers
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

data class CompanyScreenUIState (
    val companyBanner: Int = R.drawable.pizzeria_banner,
    val companyLogo: Int = R.drawable.pizzeria,
//    val companyName: String = "Company Name",
//    val companyNumber: String = "0123456789",
//    val companyRegon: String = "0123456789101",

    val company: MutableStateFlow<ResultState<Company>> = MutableStateFlow(ResultState.Idle),
    val employees: MutableStateFlow<ResultState<List<Employee>>> = MutableStateFlow(ResultState.Idle),
    val employeeImages: MutableStateFlow<MutableMap<UUID, MutableStateFlow<ResultState<ImageBitmap?>>>> = MutableStateFlow(HashMap()),
    val companyB2BCustomers: List<B2BCustomer> = testB2BCustomers,
    val companyB2CCustomers: List<Person> = testB2СCustomers,

    val changeCompanyNameBottomSheetExpanded: Boolean = false,

//    val companyAddress: Address = Address(),
//    val companyFullAddress: String = "ul. Example, 9, New-York, 50123",
    val changeCompanyAddressBottomSheetExpanded: Boolean = false,


    val addEmployeeBottomSheetExpanded: Boolean = false,
    val employeeProfileBottomSheetExpanded: Boolean = false,
    val addB2CCustomerBottomSheetExpanded: Boolean = false,
    val addB2BCustomerBottomSheetExpanded: Boolean = false,
    val customerProfileDataBottomSheetExpanded: Boolean = false,
)