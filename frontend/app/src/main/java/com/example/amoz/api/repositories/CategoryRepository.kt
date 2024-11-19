package com.example.amoz.api.repositories

import com.example.amoz.models.CategoryDetails
import com.example.amoz.models.CategoryTree
import com.example.amoz.api.requests.CategoryCreateRequest
import com.example.amoz.api.services.CategoryService
import java.util.UUID
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryService: CategoryService
) : BaseRepository() {

    suspend fun createCategory(categoryCreateRequest: CategoryCreateRequest): com.example.amoz.models.CategoryDetails? {
        return performRequest {
            categoryService.createCategory(categoryCreateRequest)
        }
    }

    suspend fun updateCategory(categoryId: UUID, categoryCreateRequest: CategoryCreateRequest): com.example.amoz.models.CategoryDetails? {
        return performRequest {
            categoryService.updateCategory(categoryId, categoryCreateRequest)
        }
    }

    suspend fun deleteCategory(categoryId: UUID) {
        performRequest {
            categoryService.deleteCategory(categoryId)
        }
    }

    suspend fun getAllCompanyCategories(): List<com.example.amoz.models.CategoryTree> {
        return performRequest {
            categoryService.getAllCompanyCategories()
        } ?: listOf()
    }
}