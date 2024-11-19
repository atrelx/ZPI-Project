package com.example.amoz.ui.screens.bottom_screens.company

import com.example.amoz.api.repositories.CompanyRepository
import com.example.amoz.api.repositories.EmployeeRepository
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.data.B2BCustomer
import com.example.amoz.data.Person
import com.example.amoz.models.Address
import com.example.amoz.models.Company
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

@HiltViewModel
class CompanyScreenViewModel @Inject constructor(
    val employeeRepository: EmployeeRepository,
    val companyRepository: CompanyRepository
)
    : BaseViewModel() {
    private val _companyUiState = MutableStateFlow(CompanyScreenUiState())
    val companyUiState: StateFlow<CompanyScreenUiState> = _companyUiState.asStateFlow()

    private val _fetchEmployeesState = MutableStateFlow<ResultState<List<Employee>>>(ResultState.Idle)
    val fetchEmployeesState: StateFlow<ResultState<List<Employee>>> = _fetchEmployeesState.asStateFlow()

    private val _fetchCompanyDetails = MutableStateFlow<ResultState<Company>>(ResultState.Idle)
//    val fetchCompanyDetails =

    fun updateCompanyAddress(
        street: String, houseNumber: String, apartmentNumber: String?,
        city: String, postalCode: String, additionalInfo: String
    ) {
        _companyUiState.value.companyAddress?.let {
            _companyUiState.update { currState ->
                val newAddress = Address(
                    addressId = it.addressId,
                    city = city,
                    street = street,
                    streetNumber = houseNumber,
                    apartmentNumber = apartmentNumber,
                    postalCode = postalCode,
                    additionalInformation = additionalInfo,
                )

                currState.copy(
                    companyAddress = newAddress,
                    companyFullAddress = buildFullAddressString(newAddress)
                )
            }
        }
    }

    fun fetchCompanyDetails() {
        performRepositoryAction(
            binding = _fetchCompanyDetails,
            failureMessage = "Sorry, nie tym razem",
            action = { companyRepository.getUserCompany() },
            onSuccess = { company ->
                _companyUiState.update { currState ->
                    currState.copy(
                        companyName = company.name,
                        companyNumber = company.companyNumber,
                        companyAddress = company.address,
                    )
                }
            }
        )
    }

    fun updateCompanyDetailsLoading(isLoading: Boolean) {
        _companyUiState.update { currState ->
            currState.copy(
                companyDetailsLoading = isLoading
            )
        }
    }

    fun fetchEmployees() {
        performRepositoryAction(_fetchEmployeesState, "Could not fetch employees. Try again later.",
            action = { employeeRepository.fetchEmployees() },
            onSuccess = { result ->

                _companyUiState.value = _companyUiState.value.copy(companyEmployees = result, companyDetailsLoading = false)
            }
        )
    }

//    fun getProfilePicture() {
//        performRepositoryAction(_getProfilePictureState, "Could fetch profile picture. Try again later.",
//            action = {
//                userRepository.getProfilePicture()
//            }
//        )
//    }


    fun updateCompanyName(name: String) {
        _companyUiState.update { currState ->
            currState.copy(companyName = name)
        }
    }

    fun updateEmploymentDate(employeeId: UUID, newDate: LocalDate) {
        /*TODO: Change employmentDate of employee*/
//        testEmployees[employeeId] = testEmployees[employeeId].copy(
//            employmentDate = newDate
//        )
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

    private fun buildFullAddressString(address: Address): String {
        return buildString {
            append("${address.street}, ${address.streetNumber}")
            address.apartmentNumber?.let { append(", $it") }
            append(", ${address.city}, ${address.postalCode}")
        }
    }

}