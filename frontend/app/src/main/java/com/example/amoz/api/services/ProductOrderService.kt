package com.example.amoz.api.services

import com.example.amoz.api.models.ProductOrderDetails
import com.example.amoz.api.models.ProductOrderSummary
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.responses.MessageResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface ProductOrderService {

    @POST("api/productOrders")
    suspend fun createProductOrder(
        @Body request: ProductOrderCreateRequest
    ): Response<ProductOrderDetails>

    @PUT("api/productOrders/{productOrderId}")
    suspend fun updateProductOrder(
        @Path("productOrderId") productOrderId: UUID,
        @Body request: ProductOrderCreateRequest
    ): Response<ProductOrderDetails>

    @PUT("api/productOrders/{productOrderId}/generateInvoice")
    suspend fun generateInvoice(
        @Path("productOrderId") productOrderId: UUID
    ): Response<Unit>

    @GET("api/productOrders/invoice/{invoiceId}")
    suspend fun getInvoiceDetails(
        @Path("invoiceId") invoiceId: UUID
    ): Response<JsonElement>

    @GET("api/productOrders/{productOrderId}")
    suspend fun getProductOrderDetails(
        @Path("productOrderId") productOrderId: UUID
    ): Response<ProductOrderDetails>

    @GET("api/productOrders")
    suspend fun getAllProductOrders(): Response<List<ProductOrderSummary>>
}
