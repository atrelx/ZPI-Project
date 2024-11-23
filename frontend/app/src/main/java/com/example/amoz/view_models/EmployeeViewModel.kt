package com.example.amoz.view_models

import android.content.Context
import com.example.amoz.api.repositories.EmployeeRepository
import com.example.amoz.api.repositories.UserRepository
import com.example.amoz.api.sealed.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject
@HiltViewModel
class EmployeeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val employeeRepository: EmployeeRepository
) : BaseViewModel() {

    val acceptInvitationState: MutableStateFlow<ResultState<Unit>> = MutableStateFlow(ResultState.Idle)

    fun acceptInvitation(token: String) {

        performRepositoryAction(acceptInvitationState, "Could not accept invitation.", action = {
                employeeRepository.acceptInvitationToCompany(token)
        })
    }
}
