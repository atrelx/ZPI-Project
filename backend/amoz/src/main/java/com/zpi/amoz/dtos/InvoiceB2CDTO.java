package com.zpi.amoz.dtos;

import com.zpi.amoz.interfaces.InvoiceDTO;
import com.zpi.amoz.models.Invoice;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import java.util.Optional;

@Schema(description = "DTO reprezentujące fakturę B2C, zawierające szczegóły faktury dla klienta indywidualnego.")
public record InvoiceB2CDTO(

        @Schema(description = "Identyfikator faktury", example = "d2fae1d8-cfbb-4e47-8f2a-15de9d5b345d")
        UUID invoiceId,

        @Schema(description = "Informacje o firmie wystawiającej fakturę",
                example = "{ \"companyId\": \"a8f6c9b5-cf77-4e4f-b13c-6f58568e92ab\", \"companyNumber\": \"9876543210\", \"name\": \"Firma ABC\", \"countryOfRegistration\": \"Polska\" }")
        CompanyDTO company,

        @Schema(description = "Informacje o kliencie indywidualnym (B2C), któremu wystawiona jest faktura",
                example = "{ \"customerId\": \"b2a3dcb9-5245-4795-9480-5031d1531c7f\", \"contactPerson\": { \"contactPersonId\": \"a56828bc-44a3-4974-83a0-2f30c2a8d5f7\", \"contactNumber\": \"+48501234567\", \"emailAddress\": \"klient@example.com\" }, \"defaultDeliveryAddress\": { \"addressId\": \"15e8b870-22e6-4bb3-bf4c-16ab4500fd4f\", \"city\": \"Kraków\", \"street\": \"ul. Testowa\", \"streetNumber\": \"45\", \"postalCode\": \"30-001\" } }")
        CustomerB2CDTO customerB2C,

        @Schema(description = "Numer faktury", example = "98765")
        int invoiceNumber,

        @Schema(description = "Kwota na fakturze", example = "250.50")
        BigDecimal amountOnInvoice,

        @Schema(description = "Data wystawienia faktury", example = "2024-11-15")
        LocalDate issueDate

) implements InvoiceDTO {

    public static InvoiceDTO toInvoiceDTO(Invoice invoice) {
        return new InvoiceB2CDTO(
                invoice.getInvoiceId(),
                CompanyDTO.toCompanyDTO(invoice.getProductOrder().getOrderItems().get(0).getProductVariant().getProduct().getCompany()),
                CustomerB2CDTO.toCustomerB2CDTO(invoice.getProductOrder().getCustomer().getCustomerB2C()),
                invoice.getInvoiceNumber(),
                invoice.getAmountOnInvoice(),
                invoice.getIssueDate()
        );
    }
}




