package com.example.amoz.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserIsRegisteredResponse(

    val isRegistered: Boolean
)