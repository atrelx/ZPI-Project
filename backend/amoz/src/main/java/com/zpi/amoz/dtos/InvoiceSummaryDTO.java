package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Invoice;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Schema(description = "DTO reprezentujące najważniejsze dane na fakturze")
public record InvoiceSummaryDTO(

        @Schema(description = "Identyfikator faktury", example = "d2fae1d8-cfbb-4e47-8f2a-15de9d5b345d")
        UUID invoiceId,

        @Schema(description = "Numer faktury", example = "98765")
        int invoiceNumber,

        @Schema(description = "Kwota na fakturze", example = "250.50")
        BigDecimal amountOnInvoice,

        @ArraySchema(schema = @Schema(description = "Zamówione przedmioty", implementation = ProductOrderItemSummaryDTO.class))
        List<ProductOrderItemSummaryDTO> orderItems,

        @Schema(description = "Data wystawienia faktury", example = "2024-11-15")
        LocalDate issueDate

) {

    public static InvoiceSummaryDTO toInvoiceSummaryDTO(Invoice invoice) {
        return new InvoiceSummaryDTO(
                invoice.getInvoiceId(),
                invoice.getInvoiceNumber(),
                invoice.getAmountOnInvoice(),
                invoice.getProductOrder().getOrderItems().stream().map(ProductOrderItemSummaryDTO::toProductOrderItemSummaryDTO).collect(Collectors.toList()),
                invoice.getIssueDate()
        );
    }
}


