package com.example.bussiness.firebase

import java.util.Calendar
import java.util.UUID

data class Expanse(
    val id: String = "",
    val title: String = "",
    val expanseValue: Double = 0.0,
    val imageUrl: String = "",
    val characteristics: Map<String, Any>
)

data class SoldProduct(
    val id: String = "", // Unique ID of the sold product
    val name: String = "", // Name of the sold product
    val salePrice: Double = 0.0,
    val saleDate: Long = Calendar.getInstance().timeInMillis, // Store as timestamp
    val imageUrl: String = "",
    val amount: Int = 0, // Amount sold
    val totalPrice: Double = 0.0, // Total price
    val characteristics: Map<String, Any> = emptyMap()
)

fun SoldProduct.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "name" to name,
        "salePrice" to salePrice,
        "saleDate" to saleDate,
        "imageUrl" to imageUrl,
        "amount" to amount,
        "totalPrice" to totalPrice,
        "characteristics" to characteristics
    )
}

data class Product(
    val id: String = "",
    var name: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var features: Map<String, Any> = emptyMap()
)

fun Product.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "name" to name,
        "price" to price,
        "imageUrl" to imageUrl,
        "characteristics" to features
    )
}


