package com.example.amoz.api.requests

import kotlinx.serialization.Serializable

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Serializable
data class AttributeCreateRequest(

    @field:NotBlank(message = "Attribute name is required")
    @field:Size(max = 50, message = "Attribute name should not exceed 50 characters")
    val attributeName: String,

    @field:Size(max = 255, message = "Attribute value should not exceed 255 characters")
    val value: String? = null
)
