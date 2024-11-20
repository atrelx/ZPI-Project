package com.example.amoz.ui.states

import com.example.amoz.data.Person
import com.example.amoz.R
import com.example.amoz.data.B2BCustomer
import com.example.amoz.models.Address
import com.example.amoz.models.Employee
import com.example.amoz.ui.screens.bottom_screens.company.customers.testB2BCustomers
import com.example.amoz.ui.screens.bottom_screens.company.customers.testB2СCustomers

data class CompanyScreenUiState (
    val companyLogo: Int = R.drawable.pizzeria,
    val companyName: String = "Company Name",
    val companyNumber: String = "0123456789",
    val companyRegon: String = "0123456789101",
    val changeCompanyNameBottomSheetExpanded: Boolean = false,

    val companyAddress: Address? = null,
    val companyFullAddress: String = "ul. Example, 9, New-York, 50123",
    val changeCompanyAddressBottomSheetExpanded: Boolean = false,

//    val companyEmployees: List<Employee> = testEmployees,
    val companyEmployees: List<Employee> = listOf(),
    val companyDetailsLoading: Boolean = true,
    val addEmployeeBottomSheetExpanded: Boolean = false,
    val employeeProfileBottomSheetExpanded: Boolean = false,

    val companyB2BCustomers: List<B2BCustomer> = testB2BCustomers,
    val companyB2CCustomers: List<Person> = testB2СCustomers,
    val addB2CCustomerBottomSheetExpanded: Boolean = false,
    val addB2BCustomerBottomSheetExpanded: Boolean = false,
    val customerProfileDataBottomSheetExpanded: Boolean = false,
)