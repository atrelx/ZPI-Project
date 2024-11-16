package com.example.amoz.services

import com.example.amoz.models.User
import com.example.amoz.requests.UserRegisterRequest
import com.example.amoz.responses.MessageResponse
import com.example.amoz.responses.UserIsRegisteredResponse
import kotlinx.serialization.json.JsonElement
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @POST("api/users")
    suspend fun registerUser(@Body request: UserRegisterRequest): Response<JsonElement>

    @PUT("api/users")
    suspend fun updateUser(@Body request: UserRegisterRequest): Response<JsonElement>

    @Multipart
    @PUT("api/users/picture")
    suspend fun uploadProfilePicture(@Part file: MultipartBody.Part): Response<JsonElement>

    @GET("api/users/picture")
    suspend fun getProfilePicture(): Response<ResponseBody>

    @GET("api/users/isRegistered")
    suspend fun isUserRegistered(): Response<UserIsRegisteredResponse>
}
