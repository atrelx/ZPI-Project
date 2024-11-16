package com.example.amoz.services

import com.example.amoz.requests.ProductCreateRequest
import com.example.amoz.responses.MessageResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface ProductService {

    @POST("api/products")
    suspend fun createProduct(
        @Body request: ProductCreateRequest
    ): Response<JsonElement>

    @PUT("api/products/{productId}")
    suspend fun updateProduct(
        @Path("productId") productId: UUID,
        @Body request: ProductCreateRequest
    ): Response<JsonElement>

    @PATCH("api/products/{productId}/mainVariant/{mainVariantId}")
    suspend fun setMainVariant(
        @Path("productId") productId: UUID,
        @Path("mainVariantId") mainVariantId: UUID
    ): Response<JsonElement>

    @PATCH("api/products/{productId}")
    suspend fun deactivateProduct(
        @Path("productId") productId: UUID
    ): Response<MessageResponse>

    @GET("api/products")
    suspend fun getAllProducts(): Response<JsonElement>

    @GET("api/products/{productId}")
    suspend fun getProductDetails(
        @Path("productId") productId: UUID
    ): Response<JsonElement>
}
