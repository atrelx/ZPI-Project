package com.example.amoz.ui.screens.bottom_screens.products.products_list

import com.example.amoz.models.CategorySummary
import com.example.amoz.models.ProductDetails
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.view_models.ProductsViewModel
import java.math.BigDecimal

class ProductListFilter {
    fun filterProductTemplates(
        templates: List<ProductSummary>,
        searchQuery: String,
        sortingType: ProductsViewModel.SortingType,
        priceFrom: BigDecimal,
        priceTo: BigDecimal?,
        category: CategorySummary?
    ): List<ProductSummary> {
        return templates
            .filter { product ->
                // Filter by search query
                (searchQuery.takeIf { it.isNotBlank() }?.let {
                    product.name.contains(it, ignoreCase = true) ||
                    product.brand?.contains(it, ignoreCase = true) ?: true
                } ?: true) &&
                    // Filter by category
                    (category?.let {
                        product.category.name == it.name
                    } ?: true) &&
                        // Filter by base price
                    (product.price in priceFrom..(priceTo ?: BigDecimal(Int.MAX_VALUE)))

            }
            .sortedWith(
                when (sortingType) {
                    ProductsViewModel.SortingType.ASCENDING_NAME -> compareBy { it.name }
                    ProductsViewModel.SortingType.DESCENDING_NAME -> compareByDescending { it.name }
                    ProductsViewModel.SortingType.ASCENDING_PRICE -> compareBy { it.price }
                    ProductsViewModel.SortingType.DESCENDING_PRICE -> compareByDescending { it.price }
                    ProductsViewModel.SortingType.NONE -> compareBy { it.productId } // Default or no sorting
                }
            )
    }

    fun filterProductVariants(
        variants: List<ProductVariantSummary>,
        searchQuery: String,
//        selectedTemplate: ProductSummary?,
        sortingType: ProductsViewModel.SortingType,
        priceFrom: BigDecimal,
        priceTo: BigDecimal?
    ): List<ProductVariantSummary> {
        return variants
            .filter { variant ->
                // Filter by search query
                (searchQuery.takeIf { it.isNotBlank() }
                    ?.let { variant.variantName.contains(it, ignoreCase = true) } ?: true) &&
                        // Filter by selected template
//                        (selectedTemplate?.let { variant.productVariantId == it.productId } ?: true) &&
                        // Filter by price range
                        (variant.variantPrice in priceFrom..(priceTo ?: BigDecimal(Int.MAX_VALUE)))
            }
            .sortedWith(
                when (sortingType) {
                    ProductsViewModel.SortingType.ASCENDING_NAME -> compareBy { it.variantName }
                    ProductsViewModel.SortingType.DESCENDING_NAME -> compareByDescending { it.variantName }
                    ProductsViewModel.SortingType.ASCENDING_PRICE -> compareBy { it.variantPrice }
                    ProductsViewModel.SortingType.DESCENDING_PRICE -> compareByDescending { it.variantPrice }
                    ProductsViewModel.SortingType.NONE -> compareBy { it.productVariantId } // Default or no sorting
                }
            )
    }
}
