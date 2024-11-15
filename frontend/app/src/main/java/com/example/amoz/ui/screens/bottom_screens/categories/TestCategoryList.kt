package com.example.amoz.ui.screens.bottom_screens.categories

import com.example.amoz.data.Category

val testCategoriesList = listOf(
    // Root categories (Level 0)
    Category(id = "1", name = "Electronics", parentCategoryId = "", categoryLevel = 0),
    Category(id = "10", name = "Fashion", parentCategoryId = "", categoryLevel = 0),
    Category(id = "20", name = "Home & Garden", parentCategoryId = "", categoryLevel = 0),

    // Level 1 categories
    Category(id = "2", name = "Phones", parentCategoryId = "1", categoryLevel = 1),
    Category(id = "3", name = "Laptops", parentCategoryId = "1", categoryLevel = 1),
    Category(id = "4", name = "Accessories", parentCategoryId = "1", categoryLevel = 1),
    Category(id = "11", name = "Men's Clothing", parentCategoryId = "10", categoryLevel = 1),
    Category(id = "12", name = "Women's Clothing", parentCategoryId = "10", categoryLevel = 1),
    Category(id = "21", name = "Furniture", parentCategoryId = "20", categoryLevel = 1),
    Category(id = "22", name = "Kitchen", parentCategoryId = "20", categoryLevel = 1),

    // Level 2 categories
    Category(id = "5", name = "Smartphones", parentCategoryId = "2", categoryLevel = 2),
    Category(id = "6", name = "Feature Phones", parentCategoryId = "2", categoryLevel = 2),
    Category(id = "7", name = "Gaming Laptops", parentCategoryId = "3", categoryLevel = 2),
    Category(id = "8", name = "Business Laptops", parentCategoryId = "3", categoryLevel = 2),
    Category(id = "13", name = "Jeans", parentCategoryId = "11", categoryLevel = 2),
    Category(id = "14", name = "Jackets", parentCategoryId = "11", categoryLevel = 2),
    Category(id = "15", name = "Dresses", parentCategoryId = "12", categoryLevel = 2),
    Category(id = "23", name = "Living Room Furniture", parentCategoryId = "21", categoryLevel = 2),
    Category(id = "24", name = "Bedroom Furniture", parentCategoryId = "21", categoryLevel = 2),
    Category(id = "25", name = "Cookware", parentCategoryId = "22", categoryLevel = 2),
    Category(id = "26", name = "Tableware", parentCategoryId = "22", categoryLevel = 2),

    // Level 3 categories
    Category(id = "9", name = "Wireless Chargers", parentCategoryId = "4", categoryLevel = 3),
    Category(id = "16", name = "Skinny Jeans", parentCategoryId = "13", categoryLevel = 3),
    Category(id = "17", name = "Denim Jackets", parentCategoryId = "14", categoryLevel = 3),
    Category(id = "18", name = "Evening Dresses", parentCategoryId = "15", categoryLevel = 3),
    Category(id = "27", name = "Sofas", parentCategoryId = "23", categoryLevel = 3),
    Category(id = "28", name = "Beds", parentCategoryId = "24", categoryLevel = 3),
    Category(id = "29", name = "Pots & Pans", parentCategoryId = "25", categoryLevel = 3),
    Category(id = "30", name = "Plates", parentCategoryId = "26", categoryLevel = 3)
)
