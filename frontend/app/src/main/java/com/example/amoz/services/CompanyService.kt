package com.example.amoz.services

import com.example.amoz.models.Company
import com.example.amoz.requests.CompanyCreateRequest
import com.example.amoz.responses.MessageResponse
import kotlinx.serialization.json.JsonElement
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface CompanyService {

    @POST("api/companies")
    suspend fun createCompany(@Body request: CompanyCreateRequest): Response<JsonElement>

    @GET("api/companies")
    suspend fun getUserCompany(): Response<JsonElement>

    @PUT("api/companies")
    suspend fun updateCompany(@Body request: CompanyCreateRequest): Response<JsonElement>

    @PATCH("api/companies")
    suspend fun deactivateCompany(): Response<MessageResponse>

    @PUT("api/companies/picture")
    suspend fun uploadCompanyProfilePicture(@Part file: MultipartBody.Part): Response<MessageResponse>

    @GET("api/companies/picture")
    suspend fun getCompanyProfilePicture(): Response<ResponseBody>
}
