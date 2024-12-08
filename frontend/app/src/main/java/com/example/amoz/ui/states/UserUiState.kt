package com.example.amoz.ui.states

import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.Employee

data class UserUiState (

    val fetchedUserInfo: ResultState<Employee> = ResultState.Idle,
)