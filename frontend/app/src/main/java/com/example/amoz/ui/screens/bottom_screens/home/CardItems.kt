package com.example.amoz.ui.screens.bottom_screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Addchart
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.ui.graphics.vector.ImageVector

data class HomeCardItem(
    val backgroundImageResource: Int,
    val cardTitle: String,
    val cardTitleIcon: ImageVector,
    val valueDescription: String,
    val value: String
)

val homeCardItemsList = listOf(
    HomeCardItem(
        backgroundImageResource = 0,
        cardTitle = "Revenues",
        cardTitleIcon = Icons.Filled.Addchart,
        valueDescription = "Total revenues:",
        value = "1234.00 PLN"
    ),
    HomeCardItem(
        backgroundImageResource = 0,
        cardTitle = "Sells",
        cardTitleIcon = Icons.Filled.BarChart,
        valueDescription = "Total sells:",
        value = "234 items"
    ),
    HomeCardItem(
        backgroundImageResource = 0,
        cardTitle = "Revenues",
        cardTitleIcon = Icons.Filled.Addchart,
        valueDescription = "Total revenues",
        value = "1234.00 PLN"
    ),
    HomeCardItem(
        backgroundImageResource = 0,
        cardTitle = "Revenues",
        cardTitleIcon = Icons.Filled.Addchart,
        valueDescription = "Total revenues",
        value = "1234.00 PLN"
    ),

)