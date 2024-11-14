package com.zpi.amoz.dtos;

import com.zpi.amoz.interfaces.InvoiceDTO;
import com.zpi.amoz.models.Invoice;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import java.util.Optional;

@Schema(description = "DTO reprezentujące fakturę B2B, zawierające szczegóły faktury dla firmy i klienta biznesowego.")
public record InvoiceB2BDTO(

        @Schema(description = "Identyfikator faktury", example = "f9c85a93-0e5c-4935-908b-474120c9cdef")
        UUID invoiceId,

        @Schema(description = "Numer faktury", example = "12345")
        int invoiceNumber,

        @Schema(description = "Informacje o firmie wystawiającej fakturę", example = "{ \"companyId\": \"b64d1982-8a74-4f91-bbfe-f63e828d8b34\", \"companyNumber\": \"123456789\", \"name\": \"Firma XYZ\", \"countryOfRegistration\": \"Polska\" }")
        CompanyDTO company,

        @Schema(description = "Informacje o kliencie biznesowym (B2B), któremu wystawiona jest faktura", example = "{ \"customerId\": \"c5a98f07-4175-4b8c-939b-9b4ad87c5635\", \"contactPerson\": { \"contactPersonId\": \"e9a8b931-8d1d-4f1f-9838-3bfe5f6b697b\", \"contactNumber\": \"+48123456789\", \"emailAddress\": \"kontakt@firma.com\" }, \"defaultDeliveryAddress\": { \"addressId\": \"15e8b870-22e6-4bb3-bf4c-16ab4500fd4f\", \"city\": \"Warszawa\", \"street\": \"ul. Przykładowa\", \"streetNumber\": \"123\", \"postalCode\": \"00-001\" } }")
        CustomerB2BDTO customerB2B,

        @Schema(description = "Kwota na fakturze", example = "1500.75")
        BigDecimal amountOnInvoice,

        @Schema(description = "Data wystawienia faktury", example = "2024-11-01")
        LocalDate issueDate

) implements InvoiceDTO {

    public static InvoiceDTO toInvoiceDTO(Invoice invoice) {
        return new InvoiceB2BDTO(
                invoice.getInvoiceId(),
                invoice.getInvoiceNumber(),
                CompanyDTO.toCompanyDTO(invoice.getProductOrder().getOrderItems().get(0).getProductVariant().getProduct().getCompany()),
                CustomerB2BDTO.toCustomerB2BDTO(invoice.getProductOrder().getCustomer().getCustomerB2B()),
                invoice.getAmountOnInvoice(),
                invoice.getIssueDate()
        );
    }
}



