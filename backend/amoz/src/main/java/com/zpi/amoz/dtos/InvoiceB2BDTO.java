package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Invoice;
import com.zpi.amoz.models.ProductOrder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import java.util.Optional;
import java.util.stream.Collectors;

@Schema(description = "DTO reprezentujące fakturę B2B, zawierające szczegóły faktury dla firmy i klienta biznesowego.")
public record InvoiceB2BDTO(

        @Schema(description = "Identyfikator faktury", example = "f9c85a93-0e5c-4935-908b-474120c9cdef")
        UUID invoiceId,

        @Schema(description = "Numer faktury", example = "12345")
        int invoiceNumber,

        @Schema(description = "Informacje o firmie wystawiającej fakturę", implementation = CompanyDTO.class)
        CompanyDTO company,

        @Schema(description = "Informacje o kliencie biznesowym (B2B), któremu wystawiona jest faktura", implementation = CustomerB2BDTO.class)
        CustomerB2BDTO customerB2B,

        @Schema(description = "Kwota na fakturze", example = "1500.75")
        BigDecimal amountOnInvoice,

        @ArraySchema(schema = @Schema(description = "Zamówione przedmioty", implementation = ProductOrderItemSummaryDTO.class))
        List<ProductOrderItemSummaryDTO> orderItems,

        @Schema(description = "Data wystawienia faktury", example = "2024-11-01")
        LocalDate issueDate

) {

    public static InvoiceB2BDTO toInvoiceB2BDTO(Invoice invoice) {
        return new InvoiceB2BDTO(
                invoice.getInvoiceId(),
                invoice.getInvoiceNumber(),
                CompanyDTO.toCompanyDTO(invoice.getProductOrder().getOrderItems().get(0).getProductVariant().getProduct().getCompany()),
                CustomerB2BDTO.toCustomerB2BDTO(invoice.getProductOrder().getCustomer().getCustomerB2B()),
                invoice.getAmountOnInvoice(),
                invoice.getProductOrder().getOrderItems().stream().map(ProductOrderItemSummaryDTO::toProductOrderItemSummaryDTO).collect(Collectors.toList()),
                invoice.getIssueDate()
        );
    }

}



