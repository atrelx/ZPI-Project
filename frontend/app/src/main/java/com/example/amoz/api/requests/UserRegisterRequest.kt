package com.example.amoz.api.requests

import com.example.amoz.interfaces.ValidatableRequest
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequest(

    val person: PersonCreateRequest,

    val contactPerson: ContactPersonCreateRequest
) : ValidatableRequest<UserRegisterRequest>()