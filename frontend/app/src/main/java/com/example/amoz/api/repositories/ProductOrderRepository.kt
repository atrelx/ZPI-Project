package com.example.amoz.api.repositories

import com.example.amoz.api.models.ProductOrderDetails
import com.example.amoz.api.models.ProductOrderSummary
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.services.ProductOrderService
import kotlinx.serialization.json.JsonElement
import java.util.UUID
import javax.inject.Inject

class ProductOrderRepository @Inject constructor(
    private val productOrderService: ProductOrderService
) : BaseRepository() {

    suspend fun createProductOrder(request: ProductOrderCreateRequest): ProductOrderDetails? {
        return performRequest {
            productOrderService.createProductOrder(request)
        }
    }

    suspend fun updateProductOrder(productOrderId: UUID, request: ProductOrderCreateRequest): ProductOrderDetails? {
        return performRequest {
            productOrderService.updateProductOrder(productOrderId, request)
        }
    }

    suspend fun generateInvoice(productOrderId: UUID) {
        performRequest {
            productOrderService.generateInvoice(productOrderId)
        }
    }

//    suspend fun getInvoiceDetails(invoiceId: UUID): JsonElement? {
//        return performRequest {
//            productOrderService.getInvoiceDetails(invoiceId)
//        }
//    }

    suspend fun getProductOrderDetails(productOrderId: UUID): ProductOrderDetails? {
        return performRequest {
            productOrderService.getProductOrderDetails(productOrderId)
        }
    }

    suspend fun getAllProductOrders(): List<ProductOrderSummary> {
        return performRequest {
            productOrderService.getAllProductOrders()
        } ?: listOf()
    }
}
