package com.example.amoz.ui.screens.bottom_screens.products.products_list

import com.example.amoz.data.ProductTemplate
import com.example.amoz.data.ProductVariant
import com.example.amoz.view_models.ProductsViewModel

class ProductListFilter {
    fun filterProductTemplates(
        templates: List<ProductTemplate>,
        searchQuery: String,
        sortingType: ProductsViewModel.SortingType,
        priceFrom: Double,
        priceTo: Double,
        category: String
    ): List<ProductTemplate> {
        return templates
            .filter { template ->
                // Filter by search query
                (searchQuery.takeIf { it.isNotBlank() }?.let {
                    template.name.contains(it, ignoreCase = true) ||
                    template.productVendor.contains(it, ignoreCase = true)
                } ?: true) &&
                    // Filter by category
                    (category.takeIf { it.isNotBlank() }?.let {
                        template.category == it
                    } ?: true) &&
                        // Filter by base price
                    (template.basePrice in priceFrom..priceTo)

            }
            .sortedWith(
                when (sortingType) {
                    ProductsViewModel.SortingType.ASCENDING_NAME -> compareBy { it.name }
                    ProductsViewModel.SortingType.DESCENDING_NAME -> compareByDescending { it.name }
                    ProductsViewModel.SortingType.ASCENDING_PRICE -> compareBy { it.basePrice }
                    ProductsViewModel.SortingType.DESCENDING_PRICE -> compareByDescending { it.basePrice }
                    ProductsViewModel.SortingType.NONE -> compareBy { it.id } // Default or no sorting
                }
            )
    }

    fun filterProductVariants(
        variants: List<ProductVariant>,
        getProductVariantTemplateById: (ProductVariant) -> ProductTemplate?,
        searchQuery: String,
        selectedTemplate: ProductTemplate?,
        sortingType: ProductsViewModel.SortingType,
        priceFrom: Double,
        priceTo: Double
    ): List<ProductVariant> {
        return variants
            .filter { variant ->
                val productVariantFullPrice =
                    (getProductVariantTemplateById(variant)?.basePrice ?: 0.0) + variant.impactOnPrice
                // Filter by search query
                (searchQuery.takeIf { it.isNotBlank() }
                    ?.let { variant.name.contains(it, ignoreCase = true) } ?: true) &&
                        // Filter by selected template
                        (selectedTemplate?.let { variant.productId == it.id } ?: true) &&
                        // Filter by price range
                        (productVariantFullPrice in priceFrom..priceTo)
            }
            .sortedWith(
                when (sortingType) {
                    ProductsViewModel.SortingType.ASCENDING_NAME -> compareBy { it.name }
                    ProductsViewModel.SortingType.DESCENDING_NAME -> compareByDescending { it.name }
                    ProductsViewModel.SortingType.ASCENDING_PRICE -> compareBy {
                        (getProductVariantTemplateById(it)?.basePrice ?: 0.0) + it.impactOnPrice
                    }
                    ProductsViewModel.SortingType.DESCENDING_PRICE -> compareByDescending {
                        (getProductVariantTemplateById(it)?.basePrice ?: 0.0) + it.impactOnPrice
                    }
                    ProductsViewModel.SortingType.NONE -> compareBy { it.id } // Default or no sorting
                }
            )
    }
}
