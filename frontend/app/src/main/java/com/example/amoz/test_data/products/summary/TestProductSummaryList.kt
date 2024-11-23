package com.example.amoz.test_data.products.summary

import com.example.amoz.models.CategorySummary
import com.example.amoz.models.ProductSummary
import com.example.amoz.test_data.products.details.testProductDetailsList


val testProductSummaryList = testProductDetailsList.map { productDetails ->
    ProductSummary(
        productId = productDetails.productId,
        name = productDetails.name,
        price = productDetails.price,
        category = CategorySummary(
            categoryId = productDetails.category.categoryId,
            name = productDetails.category.name,
            categoryLevel = productDetails.category.categoryLevel
        ),
        mainProductVariant = null, // Можно добавить связь с `testProductVariantsList`, если требуется
        description = productDetails.description,
        brand = productDetails.brand
    )
}

