package com.example.amoz.view_models

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.amoz.extensions.toMultipartBodyPart
import com.example.amoz.models.User
import com.example.amoz.api.repositories.UserRepository
import com.example.amoz.api.requests.ContactPersonCreateRequest
import com.example.amoz.api.requests.PersonCreateRequest
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.api.sealed.SyncResultState
import com.example.amoz.app.SignOutManager
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.states.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val userRepository: UserRepository,
    private val signOutManager: SignOutManager,
) : BaseViewModel() {

    private val _createUserRegisterRequestState = MutableStateFlow<SyncResultState<UserRegisterRequest>>(SyncResultState.Idle)
    val createUserRegisterRequestState: StateFlow<SyncResultState<UserRegisterRequest>> = _createUserRegisterRequestState

    private val _createPersonRequestState = MutableStateFlow<ResultState<PersonCreateRequest>>(ResultState.Idle)
    val createPersonRequestState: StateFlow<ResultState<PersonCreateRequest>> = _createPersonRequestState

    private val _createContactPersonRequestState = MutableStateFlow<ResultState<ContactPersonCreateRequest>>(ResultState.Idle)
    val createContactPersonRequestState: StateFlow<ResultState<ContactPersonCreateRequest>> = _createContactPersonRequestState

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

    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState: StateFlow<UserUiState> = _userUiState

    // ----------------------------------------------------

    private val imagePickerUri = MutableStateFlow<Uri?>(null)
    private val currentUserRegisterRequest = MutableStateFlow<UserRegisterRequest?>(null)

    init {
        observeSignOutEvent()
    }

    private fun observeSignOutEvent() {
        viewModelScope.launch {
            signOutManager.signOutEvent.collect {
                clearState()
            }
        }
    }

    private fun clearState() {
        _createUserRegisterRequestState.value = SyncResultState.Idle
        _createPersonRequestState.value = ResultState.Idle
        _createContactPersonRequestState.value = ResultState.Idle
        _registerUserState.value = ResultState.Idle
        _updateUserState.value = ResultState.Idle
        _getProfilePictureState.value = ResultState.Idle
        _uploadProfilePictureState.value = ResultState.Idle
        _isRegisteredState.value = ResultState.Idle
        _userUiState.update { UserUiState() }
    }

    fun registerUser(navController: NavHostController) {
       val validationMessage = currentUserRegisterRequest.value?.validate()
        if (validationMessage == null){
            performRepositoryAction(
                _registerUserState,
                "Could not sign up. Try again later.",
                action = {
                    currentUserRegisterRequest.value?.let {
                        userRepository.registerUser(it)
                    }
                }, onSuccess = {
                    updatePushToken()
                    uploadProfilePicture()
                    navController.navigate(Screens.NoCompany.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        } else {
            Log.e("UserViewModel", "Validation error: $validationMessage")
            throw IllegalArgumentException(validationMessage)
        }
    }

    private fun updatePushToken() {
        performRepositoryAction(null,
            action =  {
                userRepository.updatePushToken()
            })
    }

    fun uploadProfilePicture() {
        performRepositoryAction(
            _uploadProfilePictureState,
            "Could not upload profile picture. Try again later.",
            action = {
                imagePickerUri.value?.let {
                    userRepository.uploadProfilePicture(file = it.toMultipartBodyPart(context))
                }
            }
        )
    }

    fun isUserRegisteredRedirect(navController: NavHostController, onSuccess: (Boolean) -> Unit) {
        performRepositoryAction(
            binding = _isRegisteredState,
            action = {
                userRepository.isUserRegistered()
            }, onSuccess = {
                updatePushToken()
                if (!it) {
                  navController.navigate(Screens.Register.route){
                      popUpTo(0){inclusive = true}
                  }
                } else {
                    onSuccess(true)
                }
            }
        )
    }


    fun navigateUser(navController: NavHostController) {
        viewModelScope.launch {
            isUserRegisteredRedirect(navController) {}

            val state = _isRegisteredState.first {
                it !is ResultState.Loading
            }

            when (state) {
                is ResultState.Success -> {
                    if (state.data) {
                        navController.navigate(Screens.NoCompany.route){
                            popUpTo(0){inclusive = true}
                        }
                    } else {
                        navController.navigate(Screens.Register.route) {}
                    }
                }
                is ResultState.Failure -> {
                    Log.e("UserViewModel", "Error: ${state.message}")
                }
                else -> {
                    Log.w("UserViewModel", "Unexpected state: $state")
                }
            }
        }
    }

    fun updateCurrentUserRegisterRequest(personCreateRequest: PersonCreateRequest, contactPersonCreateRequest: ContactPersonCreateRequest) {
        val user = UserRegisterRequest(
            person = personCreateRequest,
            contactPerson = contactPersonCreateRequest
        )

        val validationErrorMessage = user.validate()
        if (validationErrorMessage == null) {
            _createUserRegisterRequestState.value = SyncResultState.Success(user)
            currentUserRegisterRequest.value = user
        } else {
            Log.e("UserViewModel", "Validation error: $validationErrorMessage")
            throw IllegalArgumentException(validationErrorMessage)
        }
    }

    fun updateCurrentUserImageUri(uri: Uri) {
        imagePickerUri.value = uri
    }
}
