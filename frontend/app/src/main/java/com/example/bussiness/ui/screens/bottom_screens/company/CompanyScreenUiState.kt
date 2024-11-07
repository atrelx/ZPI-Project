package com.example.bussiness.ui.screens.bottom_screens.company

import com.example.bussiness.data.Person
import com.example.bussiness.R
import com.example.bussiness.data.B2BCustomer
import com.example.bussiness.data.Employee
import com.example.bussiness.ui.screens.bottom_screens.company.customers.testB2BCustomers
import com.example.bussiness.ui.screens.bottom_screens.company.customers.testB2СCustomers
import com.example.bussiness.ui.screens.bottom_screens.company.employees.testEmployees

data class CompanyScreenUiState (
    val companyBanner: Int = R.drawable.pizzeria_banner,
    val companyLogo: Int = R.drawable.pizzeria,
    val companyName: String = "Company Name",
    val companyNumber: String = "0123456789",
    val companyRegon: String = "0123456789101",
    val changeCompanyNameBottomSheetExpanded: Boolean = false,

    val companyAddressStreet: String = "",
    val companyAddressHouseNumber: String = "",
    val companyAddressApartmentNumber: String = "",
    val companyAddressCity: String = "",
    val companyAddressPostalCode: String = "",
    val companyAddressAdditionalInfo: String = "",
    val companyFullAddress: String = "ul. Example, 9, New-York, 50123",
    val changeCompanyAddressBottomSheetExpanded: Boolean = false,


    val companyEmployees: List<Employee> = testEmployees,
    val addEmployeeBottomSheetExpanded: Boolean = false,
    val employeeProfileBottomSheetExpanded: Boolean = false,

    val companyB2BCustomers: List<B2BCustomer> = testB2BCustomers,
    val companyB2CCustomers: List<Person> = testB2СCustomers,
    val addB2CCustomerBottomSheetExpanded: Boolean = false,
    val addB2BCustomerBottomSheetExpanded: Boolean = false,
    val customerProfileDataBottomSheetExpanded: Boolean = false,
)