package com.example.amoz.services

import com.example.amoz.requests.ProductVariantCreateRequest
import com.example.amoz.responses.MessageResponse
import kotlinx.serialization.json.JsonElement
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface ProductVariantService {

    @POST("api/productVariants")
    suspend fun createProductVariant(
        @Body request: ProductVariantCreateRequest
    ): Response<JsonElement>

    @PUT("api/productVariants/{productVariantId}")
    suspend fun updateProductVariant(
        @Path("productVariantId") productVariantId: UUID,
        @Body request: ProductVariantCreateRequest
    ): Response<JsonElement>

    @PATCH("api/productVariants/{productVariantId}")
    suspend fun deactivateProductVariant(
        @Path("productVariantId") productVariantId: UUID
    ): Response<MessageResponse>

    @GET("api/productVariants/product/{productId}")
    suspend fun getAllProductVariantsByProductId(
        @Path("productId") productId: UUID
    ): Response<JsonElement>

    @GET("api/productVariants/{productVariantId}")
    suspend fun getProductVariant(
        @Path("productVariantId") productVariantId: UUID
    ): Response<JsonElement>

    @PUT("api/productVariants/picture/{productVariantId}")
    suspend fun uploadProductVariantPicture(
        @Path("productVariantId") productVariantId: UUID,
        @Part file: MultipartBody.Part
    ): Response<MessageResponse>

    @GET("api/productVariants/picture/{productVariantId}")
    suspend fun getProductVariantPicture(
        @Path("productVariantId") productVariantId: UUID
    ): Response<ResponseBody>
}