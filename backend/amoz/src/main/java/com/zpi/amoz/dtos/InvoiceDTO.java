package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record InvoiceDTO(
        UUID invoiceId,
        int invoiceNumber,
        BigDecimal amountOnInvoice,
        Optional<LocalDate> issueDate
) {
    public static InvoiceDTO toInvoiceDTO(Invoice invoice) {
        return new InvoiceDTO(
                invoice.getInvoiceId(),
                invoice.getInvoiceNumber(),
                invoice.getAmountOnInvoice(),
                Optional.ofNullable(invoice.getIssueDate())
        );
    }
}

