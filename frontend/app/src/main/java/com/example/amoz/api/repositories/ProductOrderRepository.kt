package com.example.amoz.api.repositories

import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductOrderSummary
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.services.ProductOrderService
import kotlinx.serialization.json.JsonElement
import java.util.UUID
import javax.inject.Inject

class ProductOrderRepository @Inject constructor(
    private val productOrderService: ProductOrderService
) : BaseRepository() {

    suspend fun createProductOrder(request: ProductOrderCreateRequest): com.example.amoz.models.ProductOrderDetails? {
        return performRequest {
            productOrderService.createProductOrder(request)
        }
    }

    suspend fun updateProductOrder(productOrderId: UUID, request: ProductOrderCreateRequest): com.example.amoz.models.ProductOrderDetails? {
        return performRequest {
            productOrderService.updateProductOrder(productOrderId, request)
        }
    }

    suspend fun generateInvoice(productOrderId: UUID) {
        performRequest {
            productOrderService.generateInvoice(productOrderId)
        }
    }

    suspend fun removeProductOrder(productOrderId: UUID) {
        performRequest {
            productOrderService.removeProductOrder(productOrderId)
        }
    }

//    suspend fun getInvoiceDetails(invoiceId: UUID): JsonElement? {
//        return performRequest {
//            productOrderService.getInvoiceDetails(invoiceId)
//        }
//    }

    suspend fun getProductOrderDetails(productOrderId: UUID): com.example.amoz.models.ProductOrderDetails? {
        return performRequest {
            productOrderService.getProductOrderDetails(productOrderId)
        }
    }

    suspend fun getAllProductOrders(): List<com.example.amoz.models.ProductOrderSummary> {
        return performRequest {
            productOrderService.getAllProductOrders()
        } ?: listOf()
    }
}
