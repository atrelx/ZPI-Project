package com.example.amoz.api.repositories

import com.example.amoz.models.Attribute
import com.example.amoz.api.services.AttributeService
import kotlinx.serialization.json.JsonElement
import javax.inject.Inject

class AttributeRepository @Inject constructor(
    private val attributeService: AttributeService
) : BaseRepository() {

    suspend fun getAllAttributes(): List<com.example.amoz.models.Attribute> {
        return performRequest {
            attributeService.getAllAttributes()
        } ?: listOf()
    }

    suspend fun getProductAttributes(): List<com.example.amoz.models.Attribute> {
        return performRequest {
            attributeService.getProductAttributes()
        } ?: listOf()
    }

    suspend fun getVariantAttributes(): List<com.example.amoz.models.Attribute> {
        return performRequest {
            attributeService.getVariantAttributes()
        } ?: listOf()
    }
}

