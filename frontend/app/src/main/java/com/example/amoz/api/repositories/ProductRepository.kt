package com.example.amoz.api.repositories

import com.example.amoz.api.models.ProductDetails
import com.example.amoz.api.models.ProductSummary
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.services.ProductService
import java.util.UUID
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productService: ProductService
) : BaseRepository() {

    suspend fun createProduct(request: ProductCreateRequest): ProductDetails? {
        return performRequest {
            productService.createProduct(request)
        }
    }

    suspend fun updateProduct(productId: UUID, request: ProductCreateRequest): ProductDetails? {
        return performRequest {
            productService.updateProduct(productId, request)
        }
    }

    suspend fun setMainVariant(productId: UUID, mainVariantId: UUID): ProductDetails? {
        return performRequest {
            productService.setMainVariant(productId, mainVariantId)
        }
    }

    suspend fun deactivateProduct(productId: UUID) {
        performRequest {
            productService.deactivateProduct(productId)
        }
    }

    suspend fun getAllProducts(): List<ProductSummary> {
        return performRequest {
            productService.getAllProducts()
        } ?: listOf()
    }

    suspend fun getProductDetails(productId: UUID): ProductDetails? {
        return performRequest {
            productService.getProductDetails(productId)
        }
    }
}
