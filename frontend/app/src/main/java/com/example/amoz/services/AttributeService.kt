package com.example.amoz.services

import com.example.amoz.models.Attribute
import com.example.amoz.requests.AttributeCreateRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

import kotlinx.serialization.json.JsonElement
import retrofit2.Response


interface AttributeService {

    @GET("api/attributes")
    suspend fun getAllAttributes(): Response<JsonElement>

    @GET("api/attributes/product")
    suspend fun getProductAttributes(): Response<JsonElement>

    @GET("api/attributes/variant")
    suspend fun getVariantAttributes(): Response<JsonElement>

    @POST("api/attributes")
    suspend fun createAttribute(@Body request: AttributeCreateRequest): Response<JsonElement>

    @PUT("api/attributes")
    suspend fun updateAttribute(@Body request: AttributeCreateRequest): Response<JsonElement>
}

