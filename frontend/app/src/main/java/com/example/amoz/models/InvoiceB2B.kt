package com.example.amoz.models

import com.example.amoz.interfaces.InvoiceDTO
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Serializable
data class InvoiceB2B(
    @Contextual val invoiceId: UUID,
    val invoiceNumber: Int,
    val company: Company,
    val customerB2B: CustomerB2B,
    @Contextual val amountOnInvoice: BigDecimal,
    @Contextual val issueDate: LocalDate
) : InvoiceDTO
