package com.example.amoz.ui.screens.bottom_screens.company

import androidx.lifecycle.ViewModel
import com.example.amoz.data.Address
import com.example.amoz.data.B2BCustomer
import com.example.amoz.data.Person
import com.example.amoz.ui.screens.bottom_screens.company.customers.testB2BCustomers
import com.example.amoz.ui.screens.bottom_screens.company.customers.testB2СCustomers
import com.example.amoz.ui.screens.bottom_screens.company.employees.testEmployees
import com.example.amoz.view_models.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class CompanyScreenViewModel: BaseViewModel() {
    private val _companyUiState = MutableStateFlow(CompanyScreenUiState())
    val companyUiState: StateFlow<CompanyScreenUiState> = _companyUiState.asStateFlow()

    fun updateCompanyAddress(
        street: String, houseNumber: String, apartmentNumber: String,
        city: String, postalCode: String, additionalInfo: String
    ) {
        _companyUiState.update { currState ->
            currState.copy(
                companyAddress = Address(
                    street = street,
                    houseNumber = houseNumber,
                    apartmentNumber = apartmentNumber,
                    city = city,
                    postalCode = postalCode,
                    additionalInfo = additionalInfo,
                ),
                companyFullAddress = buildFullAddressString(
                    street, houseNumber, apartmentNumber, city, postalCode
                )
            )
        }
    }

//    fun fetchEmployees() {
//        performRepositoryAction()
//    }

    fun updateCompanyName(name: String) {
        _companyUiState.update { currState ->
            currState.copy(companyName = name)
        }
    }

    fun updateEmploymentDate(employeeId: Int, newDate: LocalDate) {
        /*TODO: Change employmentDate of employee*/
        testEmployees[employeeId] = testEmployees[employeeId].copy(
            employmentDate = newDate
        )
    }

    fun updateCompanyNipRegon(nip: String, regon: String) {
        _companyUiState.update { currState ->
            currState.copy(companyNumber = nip, companyRegon = regon)
        }
    }

    fun expandCustomerProfileDataBottomSheet(expand: Boolean) {
        _companyUiState.update { currState ->
            currState.copy(customerProfileDataBottomSheetExpanded = expand)
        }
    }
    fun expandEmployeeProfileBottomSheet(expand: Boolean) {
        _companyUiState.update { currState ->
            currState.copy(employeeProfileBottomSheetExpanded = expand)
        }
    }

    fun expandChangeCompanyNameBottomSheet(expand: Boolean) {
        _companyUiState.update { currState ->
            currState.copy(changeCompanyNameBottomSheetExpanded = expand)
        }
    }

    fun expandChangeCompanyAddressBottomSheet(expand: Boolean) {
        _companyUiState.update { currState ->
            currState.copy(changeCompanyAddressBottomSheetExpanded = expand)
        }
    }

    fun expandAddEmployeeBottomSheet(expand: Boolean) {
        _companyUiState.update { currState ->
            currState.copy(addEmployeeBottomSheetExpanded = expand)
        }
    }

    fun expandAddB2BCustomerBottomSheet(isVisible: Boolean) {
        _companyUiState.update { currState ->
            currState.copy(addB2BCustomerBottomSheetExpanded = isVisible)
        }
    }

    fun expandAddB2CCustomerBottomSheet(isVisible: Boolean) {
        _companyUiState.update { currState ->
            currState.copy(addB2CCustomerBottomSheetExpanded = isVisible)
        }
    }

    fun addB2CCustomer(customerFirstName: String, customerLastName: String,
                       customerEmail: String, customerPhoneNumber: String?) {
        /*TODO: Change add b2c customer func*/
        testB2СCustomers.add(
            Person(
                firstName = customerFirstName,
                lastName = customerLastName,
                email = customerEmail,
                phoneNumber = customerPhoneNumber,
                dateOfBirth = LocalDate.of(2020, 1, 15)
            )
        )
    }

    fun addB2BCustomer(companyName: String, companyEmail: String,
                       companyAddress: String, companyIdentifier: String) {
        /*TODO: Change add b2b customer func*/
        testB2BCustomers.add(
            B2BCustomer(
                companyName = companyName,
                email = companyEmail,
                companyAddress = companyAddress,
                companyIdentifier = companyIdentifier
            )
        )
    }

    private fun buildFullAddressString(
        street: String, houseNumber: String, apartmentNumber: String,
        city: String, postalCode: String
    ): String {
        return buildString {
            append("$street, $houseNumber")
            if (apartmentNumber.isNotBlank()) append(", $apartmentNumber")
            append(", $city, $postalCode")
        }
    }

}