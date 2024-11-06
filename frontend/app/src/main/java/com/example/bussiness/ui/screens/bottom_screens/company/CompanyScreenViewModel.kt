package com.example.bussiness.ui.screens.bottom_screens.company

import androidx.lifecycle.ViewModel
import com.example.bussiness.data.B2BCustomer
import com.example.bussiness.data.Person
import com.example.bussiness.ui.screens.bottom_screens.company.customers.testB2BCustomers
import com.example.bussiness.ui.screens.bottom_screens.company.customers.testB2СCustomers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class CompanyScreenViewModel: ViewModel() {
    private val _companyUiState = MutableStateFlow(CompanyScreenUiState())
    val companyUiState: StateFlow<CompanyScreenUiState> = _companyUiState.asStateFlow()

    fun updateCompanyAddress(
        street: String, houseNumber: String, apartmentNumber: String,
        city: String, postalCode: String, additionalInfo: String
    ) {
        _companyUiState.update { currState ->
            currState.copy(
                companyAddressStreet = street,
                companyAddressHouseNumber = houseNumber,
                companyAddressApartmentNumber = apartmentNumber,
                companyAddressCity = city,
                companyAddressPostalCode = postalCode,
                companyAddressAdditionalInfo = additionalInfo,
                companyFullAddress = buildFullAddressString(
                    street, houseNumber, apartmentNumber, city, postalCode
                )
            )
        }
    }

    fun updateCompanyName(name: String) {
        _companyUiState.update { currState ->
            currState.copy(companyName = name)
        }
    }

    fun updateCompanyNipRegon(nip: String, regon: String) {
        _companyUiState.update { currState ->
            currState.copy(companyNumber = nip, companyRegon = regon)
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

    fun expandAddWorkerBottomSheet(expand: Boolean) {
        _companyUiState.update { currState ->
            currState.copy(addWorkerBottomSheetExpanded = expand)
        }
    }

    fun expandAddCustomerBottomSheet(isVisible: Boolean) {
        _companyUiState.update { currState ->
            currState.copy(addCustomerBottomSheetExpanded = isVisible)
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
                registeredSince = LocalDate.of(2020, 1, 15)
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
                companyIdentifier = companyIdentifier,
                registeredSince = LocalDate.of(2018, 9, 12)
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