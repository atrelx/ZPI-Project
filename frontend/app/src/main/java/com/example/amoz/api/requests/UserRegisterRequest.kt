package com.example.amoz.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequest(

    val person: PersonCreateRequest,

    val contactPerson: ContactPersonCreateRequest
)
