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
import com.example.amoz.api.extensions.toMultipartBodyPart
import com.example.amoz.models.User
import com.example.amoz.api.repositories.UserRepository
import com.example.amoz.api.requests.ContactPersonCreateRequest
import com.example.amoz.api.requests.PersonCreateRequest
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.api.sealed.SyncResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.validation.Validation
import javax.validation.Validator
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _createUserRegisterRequestState = MutableLiveData<SyncResultState<UserRegisterRequest>>()
    val createUserRegisterRequestState: LiveData<SyncResultState<UserRegisterRequest>> = _createUserRegisterRequestState

    private val _registerUserState = MutableLiveData<ResultState<com.example.amoz.models.User>>()
    val registerUserState: LiveData<ResultState<com.example.amoz.models.User>> = _registerUserState

    private val _updateUserState = MutableLiveData<ResultState<com.example.amoz.models.User>>()
    val updateUserState: LiveData<ResultState<com.example.amoz.models.User>> = _updateUserState

    private val _getProfilePictureState = MutableLiveData<ResultState<ImageBitmap>>()
    val getProfilePictureState: LiveData<ResultState<ImageBitmap>> = _getProfilePictureState

    private val _uploadProfilePictureState = MutableLiveData<ResultState<Unit>>()
    val uploadProfilePictureState: LiveData<ResultState<Unit>> = _uploadProfilePictureState

    private val _isRegisteredState = MutableLiveData<ResultState<Boolean>>()
    val isRegisteredState: LiveData<ResultState<Boolean>> = _isRegisteredState


    val name = MutableLiveData<String>()
    val surname = MutableLiveData<String>()
    val dateOfBirth = MutableLiveData<LocalDate>()
    val sex = MutableLiveData<Sex>()
    val contactNumber = MutableLiveData<String>()
    val emailAddress = MutableLiveData<String?>()
    val imagePickerUri = MutableLiveData<Uri?>()

    fun registerUser() {
        createUserRegisterRequest()
        assertModelIsValid(createUserRegisterRequestState) { data ->
            performRepositoryAction(_registerUserState, "Could not sign up. Try again later.",
                action = {
                    userRepository.registerUser(data)
                }, onSuccess = {
                    uploadProfilePicture()
                }
            )
        }
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
            }
        )
    }

    private fun createUserRegisterRequest() {
        val name = name.value
        val surname = surname.value
        val dateOfBirth = dateOfBirth.value
        val sex = sex.value
        val contactNumber = contactNumber.value
        val emailAddress = emailAddress.value

//        val name = "Kamil gfdd"
//        val surname = "Nowak"
//        val dateOfBirth = LocalDate.of(2000, 3, 21)
//        val sex = Sex.M
//        val contactNumber = "45443534543"
//        val emailAddress = "email@example.com"

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
