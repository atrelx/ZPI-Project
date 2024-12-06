package com.example.amoz.view_models

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.NavController
import com.example.amoz.api.repositories.EmployeeRepository
import com.example.amoz.api.repositories.UserRepository
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.extensions.toMultipartBodyPart
import com.example.amoz.ui.states.EmployeeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class EmployeeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val employeeRepository: EmployeeRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _employeeUiState = MutableStateFlow(EmployeeUiState())
    val employeeUiState: StateFlow<EmployeeUiState> = _employeeUiState

    var selectedNewImageUri: Uri? by mutableStateOf(null)
    var userImageBitmap: ImageBitmap? by mutableStateOf(null)

    init {
        fetchEmployee()
        fetchEmployeeImage()
    }

    private fun fetchEmployee() {
//        val mockEmployee = Employee(
//            employeeId = UUID.fromString("1c0b25d2-d77e-4f85-bd27-2672f72848d1"),
//            user = User(
//                userId = "118443418389427414489",
//                systemRole = SystemRole.USER
//            ),
//            contactPerson = ContactPerson(
//                contactPersonId = UUID.fromString("e4b5fa0f-b8b1-4b60-bb6b-e4b3d91811ed"),
//                contactNumber = "+48123456789",
//                emailAddress = "contact@company.com"
//            ),
//            person = Person(
//                personId = UUID.fromString("1f3d4d5e-3c11-4e53-8f82-e2f0d8c94c89"),
//                name = "Jan",
//                surname = "Kowalski",
//                dateOfBirth = LocalDate.parse("1985-04-23"),
//                sex = Sex.M
//            ),
//            roleInCompany = RoleInCompany.OWNER,
//            employmentDate = LocalDate.parse("2022-05-15")
//        )

//        _employeeUiState.value.fetchedEmployeeState.value = ResultState.Success(mockEmployee)

        performRepositoryAction(
            employeeUiState.value.fetchedEmployeeState,
            "Could not fetch employee.",
            action = {
                employeeRepository.fetchEmployee()
                     },
        )
    }

    private fun fetchEmployeeImage() {
        performRepositoryAction(
            employeeUiState.value.fetchedEmployeeImageState,
            "Could not fetch employee image.",
            action = {
                val imageBitmap = userRepository.getProfilePicture()
                userImageBitmap = imageBitmap
                imageBitmap
            }
        )
    }

     fun updateUser(userRegisterRequest: UserRegisterRequest, navController: NavController) {
        performRepositoryAction(
            null,
            "Could not update employee.",
            action = { userRepository.updateUser(userRegisterRequest) },
            onSuccess = {
                Log.d("EmployeeViewModel", "User updated")
                updateUserImage(navController)
                Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun updateUserImage(navController: NavController) {
        if (selectedNewImageUri != null) {
            performRepositoryAction(
                null,
                "Could not update user image.",
                action = { userRepository.uploadProfilePicture(selectedNewImageUri!!.toMultipartBodyPart(context)) },
                onSuccess = {
                    fetchEmployee()
                    fetchEmployeeImage()
                    navController.popBackStack()
                },
            )
        } else {
            fetchEmployee()
            navController.popBackStack()
        }
    }

    fun manageInvitation(token: String, isAccepted: Boolean) {
        if (isAccepted) {
            acceptInvitation(token)
        } else {
            declineInvitation(token)
        }
    }

    fun acceptInvitation(token: String) {
        performRepositoryAction(
            null,
            "Could not accept invitation.",
            action = { employeeRepository.acceptInvitationToCompany(token) },
            onSuccess = {
                fetchEmployee()
                fetchEmployeeImage()
            }
        )
    }

    fun declineInvitation(token: String) {
        performRepositoryAction(
            null,
            "Could not decline invitation.",
            action = { employeeRepository.rejectInvitationToCompany(token) }
        )
    }

    // ----------------------------------------------------

    fun changeProfileDropDownExpanded(isExpanded: Boolean) {
        _employeeUiState.value = _employeeUiState.value.copy(isProfileDropdownExpanded = isExpanded)
    }


}
