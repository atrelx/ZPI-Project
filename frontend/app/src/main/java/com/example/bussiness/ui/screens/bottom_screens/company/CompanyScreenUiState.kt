package com.example.bussiness.ui.screens.bottom_screens.company

import com.example.bussiness.data.Person
import com.example.bussiness.R
import com.example.bussiness.data.B2BCustomer
import com.example.bussiness.ui.screens.bottom_screens.company.customers.testB2BCustomers
import com.example.bussiness.ui.screens.bottom_screens.company.customers.testB2СCustomers
import com.example.bussiness.ui.screens.bottom_screens.company.workers.testWorkers

data class CompanyScreenUiState (
    val companyBanner: Int = R.drawable.pizzeria_banner,
    val companyLogo: Int = R.drawable.pizzeria,
    val companyName: String = "",
    val companyDescription: String = "",
    val companyNumber: String = "",
    val companyRegon: String = "",
    val companyAddress: String = "",

    val companyWorkers: List<Person> = testWorkers,
    val addWorkerBottomSheetExpanded: Boolean = false,

    val companyB2BCustomers: List<B2BCustomer> = testB2BCustomers,
    val companyB2CCustomers: List<Person> = testB2СCustomers,
    val addCustomerBottomSheetExpanded: Boolean = false,
)