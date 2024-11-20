package com.example.amoz.ui.screens.bottom_screens.company

import android.util.Log
import com.example.amoz.api.repositories.CompanyRepository
import com.example.amoz.api.repositories.EmployeeRepository
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.api.requests.CompanyCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.data.B2BCustomer
import com.example.amoz.data.Person
import com.example.amoz.extensions.updateResultState
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

@HiltViewModel
class CompanyScreenViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val companyRepository: CompanyRepository
)
    : BaseViewModel() {
    private val _companyUIState = MutableStateFlow(CompanyScreenUIState())
    val companyUIState: StateFlow<CompanyScreenUIState> = _companyUIState.asStateFlow()

    private val _companyCreateRequestState: MutableStateFlow<CompanyCreateRequest> = MutableStateFlow(CompanyCreateRequest())
    val companyCreateRequestState: StateFlow<CompanyCreateRequest> = _companyCreateRequestState.asStateFlow()

    init {
        fetchCompanyDetails()
    }

    fun createCompany() {
        val companyCreateRequest = _companyCreateRequestState.value
        val validationErrorMessage = companyCreateRequest.validate()
        if (validationErrorMessage == null) {
            performRepositoryAction(_companyUIState.value.company, "Could not create company",
                action = {
                    companyRepository.createCompany(companyCreateRequest)
                })
        } else {
            Log.w(tag, validationErrorMessage)
        }
    }

    fun updateCompany(companyCreateRequest: CompanyCreateRequest) {
        val validationErrorMessage = companyCreateRequest.validate()
        if (validationErrorMessage == null) {
            performRepositoryAction(_companyUIState.value.company, "Could not update company",
                action = {
                    companyRepository.updateCompany(companyCreateRequest)
                })
        } else {
            Log.w(tag, validationErrorMessage)
        }
    }

    fun updateCompanyAddress(request: AddressCreateRequest) {
        companyCreateRequestState.value.address = request
        updateCompany(_companyCreateRequestState.value)
    }

    fun fetchCompanyDetails() {
        performRepositoryAction(_companyUIState.value.company, "Could not fetch company details. Try again later.",
            action = {
                companyRepository.getUserCompany()
            }, onSuccess = { company ->
                _companyCreateRequestState.value = CompanyCreateRequest(company)
            })
    }

    fun fetchEmployees() {
        performRepositoryAction(_companyUIState.value.employees, "Could not fetch employees. Try again later.",
            action = {
                employeeRepository.fetchEmployees()
            })
    }

    fun getProfilePicture(employeeId: UUID) {
        val map = _companyUIState.value.employeeImages.value
        if (!map.containsKey(employeeId)) {
            map[employeeId] = MutableStateFlow(ResultState.Idle)
        }
        map[employeeId]?.let {
            performRepositoryAction(it, "Could fetch employee profile picture. Try again later.",
                action = {
                    employeeRepository.getEmployeePicture(employeeId)
                }
            )
        }
    }


    fun updateCompanyName(name: String) {
        _companyUIState.value.company.updateResultState { company ->
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
        _companyUIState.value.company.updateResultState { company ->
            company.copy(companyNumber = nip, regon = regon)
        }
    }

    fun expandCustomerProfileDataBottomSheet(expand: Boolean) {
        _companyUIState.update { currState ->
            currState.copy(customerProfileDataBottomSheetExpanded = expand)
        }
    }
    fun expandEmployeeProfileBottomSheet(expand: Boolean) {
        _companyUIState.update { currState ->
            currState.copy(employeeProfileBottomSheetExpanded = expand)
        }
    }

    fun expandChangeCompanyNameBottomSheet(expand: Boolean) {
        _companyUIState.update { currState ->
            currState.copy(changeCompanyNameBottomSheetExpanded = expand)
        }
    }

    fun expandChangeCompanyAddressBottomSheet(expand: Boolean) {
        _companyUIState.update { currState ->
            currState.copy(changeCompanyAddressBottomSheetExpanded = expand)
        }
    }

    fun expandAddEmployeeBottomSheet(expand: Boolean) {
        _companyUIState.update { currState ->
            currState.copy(addEmployeeBottomSheetExpanded = expand)
        }
    }

    fun expandAddB2BCustomerBottomSheet(isVisible: Boolean) {
        _companyUIState.update { currState ->
            currState.copy(addB2BCustomerBottomSheetExpanded = isVisible)
        }
    }

    fun expandAddB2CCustomerBottomSheet(isVisible: Boolean) {
        _companyUIState.update { currState ->
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