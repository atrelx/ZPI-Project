package com.example.amoz.view_models

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.amoz.api.enums.Sex
import com.example.amoz.api.managers.GoogleAuthManager
import com.example.amoz.extensions.toMultipartBodyPart
import com.example.amoz.models.User
import com.example.amoz.api.repositories.UserRepository
import com.example.amoz.api.requests.ContactPersonCreateRequest
import com.example.amoz.api.requests.PersonCreateRequest
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.api.sealed.SyncResultState
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.states.UserUiState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class UserViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val userRepository: UserRepository,
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

    fun changeRegisterDropDownExpanded(isExpanded: Boolean) {
        _userUiState.value = _userUiState.value.copy(isRegisterDropDownExpanded = isExpanded)
    }


    // ----------------------------------------------------

    private val imagePickerUri = MutableStateFlow<Uri?>(null)
    private val currentUserRegisterRequest = MutableStateFlow<UserRegisterRequest?>(null)

    fun registerUser(navController: NavHostController) {
//        val userRegisterRequest = UserRegisterRequest(
//            person = PersonCreateRequest(
//                name = "Oleksii",
//                surname = "Adamenko",
//                dateOfBirth = LocalDate.of(2000, 6, 11),
//                sex = Sex.M,
//                ),
//            contactPerson = ContactPersonCreateRequest(
//                contactNumber = "4899999999",
//                emailAddress = "oleada@gmail.com",
//
//            )
//        )
//
//        performRepositoryAction(
//            _registerUserState,
//            "Could not sign up. Try again later.",
//            action = {
//                userRepository.registerUser(userRegisterRequest)
//            }, onSuccess = {
//                updatePushToken()
//                uploadProfilePicture()
//                Log.d("UserViewModel", "User registered")
////                navController.navigate(Screens.NoCompany.route)
//            }
//        )


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
                Log.d("UserViewModel", "User registered")
                navController.navigate(Screens.NoCompany.route)
            }
        )
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

    fun isRegistered(navController: NavHostController) {
        performRepositoryAction(
            binding = _isRegisteredState,
            action = {
                userRepository.isUserRegistered()
            }, onSuccess = {
                updatePushToken()
                if (!it) {
                    navController.navigate(Screens.Register.route) {
                        popUpTo(0)
                    }
                }
            }
        )
    }

    fun navigateUser(navController: NavHostController) {
        viewModelScope.launch {
            isRegistered(navController)

            val state = _isRegisteredState.first {
                it !is ResultState.Loading
            }

            when (state) {
                is ResultState.Success -> {
                    if (state.data) {
                        navController.navigate(Screens.Company.route)
                    } else {
                        navController.navigate(Screens.Register.route)
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
        _createUserRegisterRequestState.value = SyncResultState.Success(user)
        currentUserRegisterRequest.value = user
    }

    fun updateCurrentUserImageUri(uri: Uri) {
        imagePickerUri.value = uri
    }
}
