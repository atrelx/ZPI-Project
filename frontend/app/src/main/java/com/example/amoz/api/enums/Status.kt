package com.example.amoz.api.enums

import kotlinx.serialization.Serializable

@Serializable
enum class Status {
    NEW, ORDERED, SHIPPED, DELIVERED, CANCELLED
}