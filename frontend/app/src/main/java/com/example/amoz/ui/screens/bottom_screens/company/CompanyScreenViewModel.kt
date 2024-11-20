package com.example.amoz.ui.screens.bottom_screens.company

import android.util.Log
import androidx.compose.runtime.collectAsState
import com.example.amoz.api.repositories.CompanyRepository
import com.example.amoz.api.repositories.EmployeeRepository
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.api.requests.CompanyCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.data.B2BCustomer
import com.example.amoz.data.Person
import com.example.amoz.extensions.fetchDataIfSuccess
import com.example.amoz.extensions.updateResultState
import com.example.amoz.models.Address
import com.example.amoz.models.Employee
import com.example.amoz.ui.screens.bottom_screens.company.customers.testB2BCustomers
import com.example.amoz.ui.screens.bottom_screens.company.customers.testB2СCustomers
import com.example.amoz.view_models.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import javax.validation.ValidationException

@HiltViewModel
class CompanyScreenViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val companyRepository: CompanyRepository
)
    : BaseViewModel() {
    private val _companyUiState = MutableStateFlow(CompanyScreenUIState())
    val companyUIState: StateFlow<CompanyScreenUIState> = _companyUiState.asStateFlow()

    private val _companyCreateRequestState: MutableStateFlow<CompanyCreateRequest> = MutableStateFlow(CompanyCreateRequest())
    val companyCreateRequestState: StateFlow<CompanyCreateRequest> = _companyCreateRequestState.asStateFlow()

    fun updateCompanyAddress(
        street: String, houseNumber: String, apartmentNumber: String?,
        city: String, postalCode: String, additionalInfo: String?
    ) {
        _companyUiState.value.company.updateResultState { company ->
            val updatedAddress = company.address.copy(
                street = street,
                streetNumber = houseNumber,
                apartmentNumber = apartmentNumber,
                city = city,
                postalCode = postalCode,
                additionalInformation = additionalInfo,
            )
            company.copy(address = updatedAddress)
        }
    }

    fun createCompany() {
        val companyCreateRequest = _companyCreateRequestState.value
        val validationErrorMessage = companyCreateRequest.validate()
        if (validationErrorMessage == null) {
            performRepositoryAction(_companyUiState.value.company, "Could not create company",
                action = {
                    companyRepository.createCompany(companyCreateRequest)
                })
        } else {
            Log.w(tag, validationErrorMessage)
        }
    }

    fun updateCompany() {
        val companyCreateRequest = _companyCreateRequestState.value
        val validationErrorMessage = companyCreateRequest.validate()
        if (validationErrorMessage == null) {
            performRepositoryAction(_companyUiState.value.company, "Could not update company",
                action = {
                    companyRepository.updateCompany(companyCreateRequest)
                })
        } else {
            Log.w(tag, validationErrorMessage)
        }
    }

    fun fetchCompanyDetails() {
        performRepositoryAction(_companyUiState.value.company, "Could not fetch company details. Try again later.",
            action = {
                companyRepository.getUserCompany()
            }, onSuccess = { company ->
                _companyCreateRequestState.value = CompanyCreateRequest(company)
            })
    }

    fun fetchEmployees() {
        performRepositoryAction(_companyUiState.value.companyEmployees, "Could not fetch employees. Try again later.",
            action = {
                employeeRepository.fetchEmployees()
            })
    }

//    fun getProfilePicture() {
//        performRepositoryAction(_getProfilePictureState, "Could fetch profile picture. Try again later.",
//            action = {
//                userRepository.getProfilePicture()
//            }
//        )
//    }


    fun updateCompanyName(name: String) {
        _companyUiState.value.company.updateResultState { company ->
            company.copy(name = name)
        }
    }

//    fun updateEmploymentDate(employeeId: UUID, newDate: LocalDate) {
//        /*TODO: Change employmentDate of employee*/
////        testEmployees[employeeId] = testEmployees[employeeId].copy(
////            employmentDate = newDate
////        )
//    }

    fun updateCompanyNipRegon(nip: String, regon: String) {
        _companyUiState.value.company.updateResultState { company ->
            company.copy(companyNumber = nip, regon = regon)
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
}