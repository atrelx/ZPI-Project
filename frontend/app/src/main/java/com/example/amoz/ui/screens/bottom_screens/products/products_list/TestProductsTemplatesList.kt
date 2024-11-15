package com.example.amoz.ui.screens.bottom_screens.products.products_list

import com.example.amoz.data.ProductTemplate
import com.example.amoz.data.ProductVariant

val testProductTemplatesList = listOf(
    ProductTemplate(
        id = "1",
        name = "Wireless Headphones",
        description = "High-quality wireless headphones with noise cancellation",
        basePrice = 129.99,
        productVendor = "SoundTech",
        attributes = mapOf(
            "Color" to "Black",
            "Battery Life" to "20 hours",
            "Weight" to "200g"
        )
    ),
    ProductTemplate(
        id = "2",
        name = "Smart Watch",
        description = "Stylish smartwatch with heart rate monitor and GPS",
        basePrice = 89.99,
        productVendor = "Wearable Inc.",
        attributes = mapOf(
            "Color" to "Silver",
            "Screen Size" to "1.5 inches",
            "Water Resistance" to "5 ATM"
        )
    ),
    ProductTemplate(
        id = "3",
        name = "Laptop Stand",
        description = "Ergonomic aluminum laptop stand with adjustable height",
        basePrice = 39.99,
        productVendor = "DeskMate",
        attributes = mapOf(
            "Color" to "Gray",
            "Material" to "Aluminum",
            "Weight Capacity" to "5 kg"
        )
    ),
    ProductTemplate(
        id = "4",
        name = "Electric Toothbrush",
        description = "Rechargeable electric toothbrush with multiple brushing modes",
        basePrice = 59.99,
        productVendor = "SmileCare",
        attributes = mapOf(
            "Color" to "Blue",
            "Battery Life" to "2 weeks",
            "Brushing Modes" to "3"
        )
    ),
    ProductTemplate(
        id = "5",
        name = "Portable Projector",
        description = "Compact projector for home theater experience",
        basePrice = 249.99,
        productVendor = "VisionTech",
        attributes = mapOf(
            "Resolution" to "1080p",
            "Brightness" to "3000 lumens",
            "Color" to "White"
        )
    ),
    ProductTemplate(
        id = "6",
        name = "Bluetooth Speaker",
        description = "Portable Bluetooth speaker with high bass output",
        basePrice = 45.99,
        productVendor = "AudioHub",
        attributes = mapOf(
            "Color" to "Red",
            "Battery Life" to "12 hours",
            "Weight" to "500g"
        )
    ),
    ProductTemplate(
        id = "7",
        name = "Smart Light Bulb",
        description = "Wi-Fi enabled light bulb with color changing features",
        basePrice = 14.99,
        productVendor = "BrightHome",
        attributes = mapOf(
            "Color" to "Multi-color",
            "Wattage" to "9W",
            "Lifespan" to "20,000 hours"
        )
    ),
    ProductTemplate(
        id = "8",
        name = "Yoga Mat",
        description = "Non-slip yoga mat for home workouts",
        basePrice = 19.99,
        productVendor = "FitZone",
        attributes = mapOf(
            "Color" to "Purple",
            "Material" to "Eco-friendly PVC",
            "Thickness" to "6mm"
        )
    ),
    ProductTemplate(
        id = "9",
        name = "Running Shoes",
        description = "Comfortable running shoes with breathable mesh",
        basePrice = 79.99,
        productVendor = "ActiveFeet",
        attributes = mapOf(
            "Color" to "Blue",
            "Size" to "42",
            "Weight" to "300g"
        )
    ),
    ProductTemplate(
        id = "10",
        name = "Wireless Charger",
        description = "Fast wireless charger for Qi-enabled devices",
        basePrice = 24.99,
        productVendor = "ChargeMax",
        attributes = mapOf(
            "Color" to "Black",
            "Output Power" to "10W",
            "Cable Length" to "1m"
        )
    )
)

val testProductVariantsList = listOf(
    ProductVariant(
        productId = "1",
        barcode = 1234567891,
        name = "Wireless Headphones - Black",
        impactOnPrice = 10.00,
        image = null,
        attributes = mapOf(
            "Color" to "Black",
            "Battery Life" to "20 hours",
            "Weight" to "200g"
        )
    ),
    ProductVariant(
        productId = "1",
        barcode = 1234567891,
        name = "Wireless Headphones - Yellow",
        impactOnPrice = 10.00,
        image = null,
        attributes = mapOf(
            "Color" to "Yellow",
            "Battery Life" to "20 hours",
            "Weight" to "200g"
        )
    ),
    ProductVariant(
        productId = "2",
        barcode = 1234567892,
        name = "Smart Watch - Silver",
        impactOnPrice = 5.00,
        image = null,
        attributes = mapOf(
            "Color" to "Silver",
            "Screen Size" to "1.5 inches",
            "Water Resistance" to "5 ATM"
        )
    ),
    ProductVariant(
        productId = "3",
        barcode = 1234567893,
        name = "Laptop Stand - Gray",
        impactOnPrice = 3.00,
        image = null,
        attributes = mapOf(
            "Color" to "Gray",
            "Material" to "Aluminum",
            "Weight Capacity" to "5 kg"
        )
    ),
    ProductVariant(
        productId = "4",
        barcode = 1234567894,
        name = "Electric Toothbrush - Blue",
        impactOnPrice = 6.00,
        image = null,
        attributes = mapOf(
            "Color" to "Blue",
            "Battery Life" to "2 weeks",
            "Brushing Modes" to "3"
        )
    ),
    ProductVariant(
        productId = "5",
        barcode = 1234567895,
        name = "Portable Projector - White",
        impactOnPrice = 20.00,
        image = null,
        attributes = mapOf(
            "Resolution" to "1080p",
            "Brightness" to "3000 lumens",
            "Color" to "White"
        )
    ),
    ProductVariant(
        productId = "6",
        barcode = 1234567896,
        name = "Bluetooth Speaker - Red",
        impactOnPrice = 4.00,
        image = null,
        attributes = mapOf(
            "Color" to "Red",
            "Battery Life" to "12 hours",
            "Weight" to "500g"
        )
    ),
    ProductVariant(
        productId = "7",
        barcode = 1234567897,
        name = "Smart Light Bulb - Multi-color",
        impactOnPrice = 2.00,
        image = null,
        attributes = mapOf(
            "Color" to "Multi-color",
            "Wattage" to "9W",
            "Lifespan" to "20,000 hours"
        )
    ),
    ProductVariant(
        productId = "8",
        barcode = 1234567898,
        name = "Yoga Mat - Purple",
        impactOnPrice = 1.00,
        image = null,
        attributes = mapOf(
            "Color" to "Purple",
            "Material" to "Eco-friendly PVC",
            "Thickness" to "6mm"
        )
    ),
    ProductVariant(
        productId = "9",
        barcode = 1234567899,
        name = "Running Shoes - Blue",
        impactOnPrice = 8.00,
        image = null,
        attributes = mapOf(
            "Color" to "Blue",
            "Size" to "42",
            "Weight" to "300g"
        )
    ),
    ProductVariant(
        productId = "10",
        barcode = 1234567810,
        name = "Wireless Charger - Black",
        impactOnPrice = 3.00,
        image = null,
        attributes = mapOf(
            "Color" to "Black",
            "Output Power" to "10W",
            "Cable Length" to "1m"
        )
    )
)

