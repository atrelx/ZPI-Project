package com.example.bussiness.ui.screens.bottom_screens.company

import androidx.compose.ui.res.stringResource
import com.example.bussiness.R
import com.example.bussiness.ui.screens.Screens
import com.example.bussiness.ui.screens.bottom_screens.company.workers.Person
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
    val addWorkerBottomSheetExpanded: Boolean = false
)