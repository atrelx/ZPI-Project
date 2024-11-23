package com.example.amoz.test_data.products.details

import com.example.amoz.models.Attribute
import com.example.amoz.models.CategoryDetails
import com.example.amoz.models.ProductAttribute
import com.example.amoz.models.ProductDetails
import java.math.BigDecimal
import java.util.UUID

val testProductDetailsList = listOf(
    ProductDetails(
        productId = UUID.fromString("11111111-1111-1111-1111-111111111111"),
        name = "Wireless Headphones",
        description = "High-quality wireless headphones with noise cancellation",
        price = BigDecimal("129.99"),
        brand = "SoundTech",
        category = CategoryDetails(
            categoryId = UUID.fromString("22222222-2222-2222-2222-222222222222"),
            name = "Electronics",
            categoryLevel = 1
        ),
        productAttributes = listOf(
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333333"),
                attribute = Attribute("Color"),
                value = "Black"
            ),
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333334"),
                attribute = Attribute("Battery Life"),
                value = "20 hours"
            ),
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333335"),
                attribute = Attribute("Weight"),
                value = "200g"
            )
        )
    ),
    ProductDetails(
        productId = UUID.fromString("11111111-1111-1111-1111-111111111112"),
        name = "Smart Watch",
        description = "Stylish smartwatch with heart rate monitor and GPS",
        price = BigDecimal("89.99"),
        brand = "Wearable Inc.",
        category = CategoryDetails(
            categoryId = UUID.fromString("22222222-2222-2222-2222-222222222223"),
            name = "Wearables",
            categoryLevel = 1
        ),
        productAttributes = listOf(
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333336"),
                attribute = Attribute("Color"),
                value = "Silver"
            ),
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333337"),
                attribute = Attribute("Screen Size"),
                value = "1.5 inches"
            ),
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333338"),
                attribute = Attribute("Water Resistance"),
                value = "5 ATM"
            )
        )
    ),
    ProductDetails(
        productId = UUID.fromString("11111111-1111-1111-1111-111111111113"),
        name = "Gaming Keyboard",
        description = "Mechanical RGB keyboard with customizable keys",
        price = BigDecimal("79.99"),
        brand = "GameTech",
        category = CategoryDetails(
            categoryId = UUID.fromString("22222222-2222-2222-2222-222222222224"),
            name = "Accessories",
            categoryLevel = 1
        ),
        productAttributes = listOf(
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333339"),
                attribute = Attribute("Switch Type"),
                value = "Mechanical"
            ),
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333340"),
                attribute = Attribute("Lighting"),
                value = "RGB"
            ),
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333341"),
                attribute = Attribute("Connectivity"),
                value = "Wired"
            )
        )
    ),
    ProductDetails(
        productId = UUID.fromString("11111111-1111-1111-1111-111111111114"),
        name = "Electric Kettle",
        description = "Fast-boil electric kettle with temperature control",
        price = BigDecimal("49.99"),
        brand = "HomeEssentials",
        category = CategoryDetails(
            categoryId = UUID.fromString("22222222-2222-2222-2222-222222222225"),
            name = "Kitchen Appliances",
            categoryLevel = 1
        ),
        productAttributes = listOf(
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333342"),
                attribute = Attribute("Capacity"),
                value = "1.7L"
            ),
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333343"),
                attribute = Attribute("Material"),
                value = "Stainless Steel"
            ),
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333344"),
                attribute = Attribute("Power"),
                value = "1500W"
            )
        )
    ),
    ProductDetails(
        productId = UUID.fromString("11111111-1111-1111-1111-111111111115"),
        name = "Wireless Mouse",
        description = "Ergonomic wireless mouse with adjustable DPI",
        price = BigDecimal("29.99"),
        brand = "TechEase",
        category = CategoryDetails(
            categoryId = UUID.fromString("22222222-2222-2222-2222-222222222226"),
            name = "Accessories",
            categoryLevel = 1
        ),
        productAttributes = listOf(
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333345"),
                attribute = Attribute("DPI"),
                value = "800-1600"
            ),
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333346"),
                attribute = Attribute("Battery Life"),
                value = "6 months"
            ),
            ProductAttribute(
                productAttributeId = UUID.fromString("33333333-3333-3333-3333-333333333347"),
                attribute = Attribute("Color"),
                value = "Black"
            )
        )
    )
)