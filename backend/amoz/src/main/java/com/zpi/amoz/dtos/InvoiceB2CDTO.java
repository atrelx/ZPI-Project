package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Invoice;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import java.util.Optional;
import java.util.stream.Collectors;

@Schema(description = "DTO reprezentujące fakturę B2C, zawierające szczegóły faktury dla klienta indywidualnego.")
public record InvoiceB2CDTO(

        @Schema(description = "Identyfikator faktury", example = "d2fae1d8-cfbb-4e47-8f2a-15de9d5b345d")
        UUID invoiceId,

        @Schema(description = "Informacje o firmie wystawiającej fakturę", implementation = CompanyDTO.class)
        CompanyDTO company,

        @Schema(description = "Informacje o kliencie indywidualnym (B2C), któremu wystawiona jest faktura", implementation = CustomerB2CDTO.class)
        CustomerB2CDTO customerB2C,

        @Schema(description = "Numer faktury", example = "98765")
        int invoiceNumber,

        @Schema(description = "Kwota na fakturze", example = "250.50")
        BigDecimal amountOnInvoice,

        @ArraySchema(schema = @Schema(description = "Zamówione przedmioty", implementation = ProductOrderItemSummaryDTO.class))
        List<ProductOrderItemSummaryDTO> orderItems,

        @Schema(description = "Data wystawienia faktury", example = "2024-11-15")
        LocalDate issueDate

) {

    public static InvoiceB2CDTO toInvoiceB2CDTO(Invoice invoice) {
        return new InvoiceB2CDTO(
                invoice.getInvoiceId(),
                CompanyDTO.toCompanyDTO(invoice.getProductOrder().getOrderItems().get(0).getProductVariant().getProduct().getCompany()),
                CustomerB2CDTO.toCustomerB2CDTO(invoice.getProductOrder().getCustomer().getCustomerB2C()),
                invoice.getInvoiceNumber(),
                invoice.getAmountOnInvoice(),
                invoice.getProductOrder().getOrderItems().stream().map(ProductOrderItemSummaryDTO::toProductOrderItemSummaryDTO).collect(Collectors.toList()),
                invoice.getIssueDate()
        );
    }
}




