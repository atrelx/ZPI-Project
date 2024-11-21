package com.example.amoz.test_data.products.summary

import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.test_data.products.details.testProductVariantDetailsList

val testProductVariantSummariesList = testProductVariantDetailsList.map {
    ProductVariantSummary(
        productVariantId = it.productVariantId,
        code = it.code,
        variantPrice = it.variantPrice,
        variantName = it.variantName
    )
}
