package com.example.amoz.test_data.products.details

import com.example.amoz.api.enums.UnitDimensions
import com.example.amoz.api.enums.UnitWeight
import com.example.amoz.models.Attribute
import com.example.amoz.models.Dimensions
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.models.Stock
import com.example.amoz.models.VariantAttribute
import com.example.amoz.models.Weight
import java.math.BigDecimal
import java.util.UUID

val testProductVariantDetailsList = listOf(
    ProductVariantDetails(
        productVariantId = UUID.fromString("44444444-4444-4444-4444-444444444444"),
        code = 1001,
        stock = Stock(
            stockId = UUID.fromString("55555555-5555-5555-5555-555555555555"),
            amountAvailable = 50,
            alarmingAmount = 5,
            isAlarmTriggered = false
        ),
        dimensions = Dimensions(
            dimensionsId = UUID.fromString("66666666-6666-6666-6666-666666666666"),
            unitDimensions = UnitDimensions.CM,
            height = 15.0,
            length = 20.0,
            width = 10.0
        ),
        weight = Weight(
            weightId = UUID.fromString("77777777-7777-7777-7777-777777777777"),
            unitWeight = UnitWeight.KG,
            amount = 1.2
        ),
        variantPrice = BigDecimal("139.99"),
        variantName = "Wireless Headphones - Black",
        variantAttributes = listOf(
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888888"),
                attribute = Attribute("Color"),
                value = "Black"
            ),
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888889"),
                attribute = Attribute("Battery Life"),
                value = "20 hours"
            )
        )
    ),
    ProductVariantDetails(
        productVariantId = UUID.fromString("44444444-4444-4444-4444-444444444445"),
        code = 1002,
        stock = Stock(
            stockId = UUID.fromString("55555555-5555-5555-5555-555555555556"),
            amountAvailable = 30,
            alarmingAmount = 3,
            isAlarmTriggered = false
        ),
        dimensions = Dimensions(
            dimensionsId = UUID.fromString("66666666-6666-6666-6666-666666666667"),
            unitDimensions = UnitDimensions.CM,
            height = 5.0,
            length = 25.0,
            width = 1.5
        ),
        weight = Weight(
            weightId = UUID.fromString("77777777-7777-7777-7777-777777777778"),
            unitWeight = UnitWeight.G,
            amount = 150.0
        ),
        variantPrice = BigDecimal("99.99"),
        variantName = "Smart Watch - Silver",
        variantAttributes = listOf(
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888890"),
                attribute = Attribute("Color"),
                value = "Silver"
            ),
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888891"),
                attribute = Attribute("Screen Size"),
                value = "1.5 inches"
            )
        )
    ),
    ProductVariantDetails(
        productVariantId = UUID.fromString("44444444-4444-4444-4444-444444444446"),
        code = 1003,
        stock = Stock(
            stockId = UUID.fromString("55555555-5555-5555-5555-555555555557"),
            amountAvailable = 100,
            alarmingAmount = 10,
            isAlarmTriggered = false
        ),
        dimensions = Dimensions(
            dimensionsId = UUID.fromString("66666666-6666-6666-6666-666666666668"),
            unitDimensions = UnitDimensions.MM,
            height = 200.0,
            length = 300.0,
            width = 150.0
        ),
        weight = Weight(
            weightId = UUID.fromString("77777777-7777-7777-7777-777777777779"),
            unitWeight = UnitWeight.KG,
            amount = 0.5
        ),
        variantPrice = BigDecimal("49.99"),
        variantName = "Gaming Keyboard - RGB",
        variantAttributes = listOf(
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888892"),
                attribute = Attribute("Switch Type"),
                value = "Mechanical"
            ),
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888893"),
                attribute = Attribute("Lighting"),
                value = "RGB"
            )
        )
    ),
    ProductVariantDetails(
        productVariantId = UUID.fromString("44444444-4444-4444-4444-444444444447"),
        code = 1004,
        stock = Stock(
            stockId = UUID.fromString("55555555-5555-5555-5555-555555555558"),
            amountAvailable = 70,
            alarmingAmount = 7,
            isAlarmTriggered = false
        ),
        dimensions = Dimensions(
            dimensionsId = UUID.fromString("66666666-6666-6666-6666-666666666669"),
            unitDimensions = UnitDimensions.CM,
            height = 25.0,
            length = 30.0,
            width = 20.0
        ),
        weight = Weight(
            weightId = UUID.fromString("77777777-7777-7777-7777-777777777780"),
            unitWeight = UnitWeight.KG,
            amount = 2.5
        ),
        variantPrice = BigDecimal("199.99"),
        variantName = "Portable Projector - 1080p",
        variantAttributes = listOf(
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888894"),
                attribute = Attribute("Resolution"),
                value = "1080p"
            ),
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888895"),
                attribute = Attribute("Brightness"),
                value = "3000 lumens"
            )
        )
    ),
    ProductVariantDetails(
        productVariantId = UUID.fromString("44444444-4444-4444-4444-444444444448"),
        code = 1005,
        stock = Stock(
            stockId = UUID.fromString("55555555-5555-5555-5555-555555555559"),
            amountAvailable = 40,
            alarmingAmount = 4,
            isAlarmTriggered = false
        ),
        dimensions = Dimensions(
            dimensionsId = UUID.fromString("66666666-6666-6666-6666-666666666670"),
            unitDimensions = UnitDimensions.MM,
            height = 200.0,
            length = 150.0,
            width = 50.0
        ),
        weight = Weight(
            weightId = UUID.fromString("77777777-7777-7777-7777-777777777781"),
            unitWeight = UnitWeight.G,
            amount = 800.0
        ),
        variantPrice = BigDecimal("29.99"),
        variantName = "Yoga Mat - Non-slip",
        variantAttributes = listOf(
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888896"),
                attribute = Attribute("Color"),
                value = "Purple"
            ),
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888897"),
                attribute = Attribute("Thickness"),
                value = "6mm"
            )
        )
    ),
    ProductVariantDetails(
        productVariantId = UUID.fromString("44444444-4444-4444-4444-444444444449"),
        code = 1006,
        stock = Stock(
            stockId = UUID.fromString("55555555-5555-5555-5555-555555555560"),
            amountAvailable = 25,
            alarmingAmount = 2,
            isAlarmTriggered = false
        ),
        dimensions = Dimensions(
            dimensionsId = UUID.fromString("66666666-6666-6666-6666-666666666671"),
            unitDimensions = UnitDimensions.CM,
            height = 10.0,
            length = 12.0,
            width = 5.0
        ),
        weight = Weight(
            weightId = UUID.fromString("77777777-7777-7777-7777-777777777782"),
            unitWeight = UnitWeight.KG,
            amount = 0.7
        ),
        variantPrice = BigDecimal("59.99"),
        variantName = "Electric Toothbrush - Blue",
        variantAttributes = listOf(
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888898"),
                attribute = Attribute("Brushing Modes"),
                value = "3"
            ),
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888899"),
                attribute = Attribute("Battery Life"),
                value = "2 weeks"
            )
        )
    ),
    ProductVariantDetails(
        productVariantId = UUID.fromString("44444444-4444-4444-4444-444444444450"),
        code = 1007,
        stock = Stock(
            stockId = UUID.fromString("55555555-5555-5555-5555-555555555561"),
            amountAvailable = 120,
            alarmingAmount = 12,
            isAlarmTriggered = false
        ),
        dimensions = Dimensions(
            dimensionsId = UUID.fromString("66666666-6666-6666-6666-666666666672"),
            unitDimensions = UnitDimensions.CM,
            height = 5.0,
            length = 5.0,
            width = 5.0
        ),
        weight = Weight(
            weightId = UUID.fromString("77777777-7777-7777-7777-777777777783"),
            unitWeight = UnitWeight.G,
            amount = 200.0
        ),
        variantPrice = BigDecimal("14.99"),
        variantName = "Smart Light Bulb - Multi-color",
        variantAttributes = listOf(
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888900"),
                attribute = Attribute("Color"),
                value = "Multi-color"
            ),
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888901"),
                attribute = Attribute("Wattage"),
                value = "9W"
            )
        )
    ),
    ProductVariantDetails(
        productVariantId = UUID.fromString("44444444-4444-4444-4444-444444444451"),
        code = 1008,
        stock = Stock(
            stockId = UUID.fromString("55555555-5555-5555-5555-555555555562"),
            amountAvailable = 300,
            alarmingAmount = 20,
            isAlarmTriggered = false
        ),
        dimensions = Dimensions(
            dimensionsId = UUID.fromString("66666666-6666-6666-6666-666666666673"),
            unitDimensions = UnitDimensions.CM,
            height = 8.0,
            length = 8.0,
            width = 2.0
        ),
        weight = Weight(
            weightId = UUID.fromString("77777777-7777-7777-7777-777777777784"),
            unitWeight = UnitWeight.G,
            amount = 150.0
        ),
        variantPrice = BigDecimal("24.99"),
        variantName = "Wireless Charger - Fast",
        variantAttributes = listOf(
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888902"),
                attribute = Attribute("Output Power"),
                value = "10W"
            ),
            VariantAttribute(
                variantAttributeId = UUID.fromString("88888888-8888-8888-8888-888888888903"),
                attribute = Attribute("Cable Length"),
                value = "1m"
            )
        )
    )
)