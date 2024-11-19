package com.example.amoz.models

import com.example.amoz.api.interfaces.InvoiceDTO
import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.LocalDateSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Serializable
data class InvoiceB2C(
    @Serializable(with = UUIDSerializer::class)
    val invoiceId: UUID,
    val company: com.example.amoz.models.Company,
    val customerB2C: com.example.amoz.models.CustomerB2C,
    val invoiceNumber: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val amountOnInvoice: BigDecimal,
    @Serializable(with = LocalDateSerializer::class)
    val issueDate: LocalDate
) : InvoiceDTO