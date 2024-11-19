package com.example.amoz.api.models

import com.example.amoz.api.interfaces.InvoiceDTO
import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.LocalDateSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Serializable
data class InvoiceB2B(
    @Serializable(with = UUIDSerializer::class)
    val invoiceId: UUID,
    val invoiceNumber: Int,
    val company: Company,
    val customerB2B: CustomerB2B,
    @Serializable(with = BigDecimalSerializer::class)
    val amountOnInvoice: BigDecimal,
    @Serializable(with = LocalDateSerializer::class)
    val issueDate: LocalDate
) : InvoiceDTO
