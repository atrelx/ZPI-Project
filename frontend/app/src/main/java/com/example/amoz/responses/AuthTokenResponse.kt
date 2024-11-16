package com.example.amoz.responses

import kotlinx.serialization.Serializable


@Serializable
data class AuthTokenResponse(

    val accessToken: String,

    val refreshToken: String
)
