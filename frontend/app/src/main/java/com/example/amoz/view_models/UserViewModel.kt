package com.example.amoz.view_models

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amoz.api.enums.Sex
import com.example.amoz.extensions.toMultipartBodyPart
import com.example.amoz.models.User
import com.example.amoz.api.repositories.UserRepository
import com.example.amoz.api.requests.ContactPersonCreateRequest
import com.example.amoz.api.requests.PersonCreateRequest
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.api.sealed.SyncResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _createUserRegisterRequestState = MutableStateFlow<SyncResultState<UserRegisterRequest>>(SyncResultState.Idle)
    val createUserRegisterRequestState: StateFlow<SyncResultState<UserRegisterRequest>> = _createUserRegisterRequestState

    private val _registerUserState = MutableStateFlow<ResultState<User>>(ResultState.Idle)
    val registerUserState: StateFlow<ResultState<User>> = _registerUserState

    private val _updateUserState = MutableStateFlow<ResultState<User>>(ResultState.Idle)
    val updateUserState: StateFlow<ResultState<User>> = _updateUserState

    private val _getProfilePictureState = MutableStateFlow<ResultState<ImageBitmap>>(ResultState.Idle)
    val getProfilePictureState: StateFlow<ResultState<ImageBitmap>> = _getProfilePictureState

    private val _uploadProfilePictureState = MutableStateFlow<ResultState<Unit>>(ResultState.Idle)
    val uploadProfilePictureState: StateFlow<ResultState<Unit>> = _uploadProfilePictureState

    private val _isRegisteredState = MutableStateFlow<ResultState<Boolean>>(ResultState.Idle)
    val isRegisteredState: StateFlow<ResultState<Boolean>> = _isRegisteredState


    val name = MutableStateFlow<String>("")
    val surname = MutableStateFlow<String>("")
    val dateOfBirth = MutableStateFlow<LocalDate>(LocalDate.now())
    val sex = MutableStateFlow<Sex>(Sex.M)
    val contactNumber = MutableStateFlow<String>("")
    val emailAddress = MutableStateFlow<String?>(null)
    val imagePickerUri = MutableStateFlow<Uri?>(null)

    fun registerUser() {
        createUserRegisterRequest()
        assertModelIsValid(createUserRegisterRequestState) { data ->
            performRepositoryAction(_registerUserState, "Could not sign up. Try again later.",
                action = {
                    userRepository.registerUser(data)
                }, onSuccess = {
                    updatePushToken()
                    uploadProfilePicture()
                }
            )
        }
    }

    private fun updatePushToken() {
        performRepositoryAction(null,
            action =  {
                userRepository.updatePushToken()
            })
    }

    fun updateUser() {
        createUserRegisterRequest()
        assertModelIsValid(createUserRegisterRequestState) { data ->
            performRepositoryAction(_updateUserState, "Could not update user. Try again later.",
                action = {
                    userRepository.updateUser(data)
                }
            )
        }
    }

    fun getProfilePicture() {
        performRepositoryAction(_getProfilePictureState, "Could fetch profile picture. Try again later.",
            action = {
                userRepository.getProfilePicture()
            }
        )
    }

    fun uploadProfilePicture() {
        performRepositoryAction(_uploadProfilePictureState, "Could not upload profile picture. Try again later.",
            action = {
                imagePickerUri.value?.let {
                    userRepository.uploadProfilePicture(file = it.toMultipartBodyPart(context))
                }
            }
        )
    }

    fun isRegistered() {
        performRepositoryAction(_isRegisteredState,
            action = {
                userRepository.isUserRegistered()
            }, onSuccess = {
                updatePushToken()
            }
        )
    }

    private fun createUserRegisterRequest() {
//        val name = name.value
//        val surname = surname.value
//        val dateOfBirth = dateOfBirth.value
//        val sex = sex.value
//        val contactNumber = contactNumber.value
//        val emailAddress = emailAddress.value

        val name = "Andrii"
        val surname = "Drobitko"
        val dateOfBirth = LocalDate.of(2000, 3, 21)
        val sex = Sex.M
        val contactNumber = "48123456789"
        val emailAddress = "anddro458@gmai.com"

        if (name == null ||
            surname == null ||
            dateOfBirth == null ||
            sex == null ||
            contactNumber == null) {
            _createUserRegisterRequestState.value = SyncResultState.Failure("Please fulfil all required fields")
            return
        }

        val person = PersonCreateRequest(
            name = name,
            surname = surname,
            dateOfBirth = dateOfBirth,
            sex = sex
        )

        val contactPerson = ContactPersonCreateRequest(
            contactNumber = contactNumber,
            emailAddress = emailAddress
        )


        val user = UserRegisterRequest(
            person = person,
            contactPerson = contactPerson
        )

//        val validator: Validator = Validation.buildDefaultValidatorFactory().validator
//
//        val personViolations = validator.validate(person)
//        val contactPersonViolations = validator.validate(contactPerson)
//        val userViolations = validator.validate(user)
//
//        if (personViolations.isNotEmpty() || contactPersonViolations.isNotEmpty() || userViolations.isNotEmpty()) {
//            val violationsMessages= mutableListOf<String>()
//            personViolations.forEach { violationsMessages.add(it.message) }
//            contactPersonViolations.forEach { violationsMessages.add(it.message) }
//            userViolations.forEach { violationsMessages.add(it.message) }
//
//            _createUserRegisterRequestState.value = SyncResultState.Failure("Validation failed: ${violationsMessages.first()}")
//            return
//        }
        _createUserRegisterRequestState.value = SyncResultState.Success(user)
    }
}
