package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.Status;
import com.zpi.amoz.interfaces.InvoiceDTO;
import com.zpi.amoz.models.ProductOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Schema(description = "DTO reprezentujące szczegóły zamówienia produktu, w tym dane klienta, adres wysyłki, faktura oraz pozycje zamówienia.")
public record ProductOrderDetailsDTO(

        @Schema(description = "Identyfikator zamówienia", example = "c7b87db6-bf19-4e36-8c77-8fa6d9b4b8b3")
        UUID productOrderId,

        @Schema(description = "Status zamówienia", example = "ORDERED")
        Status status,

        @Schema(description = "Informacje o kliencie składającym zamówienie", nullable = true,
                example = "{\"customerId\": \"b88aef3c-7c70-4c33-9980-d96e6849a0ea\", \"contactPerson\": {\"contactPersonId\": \"b7fc9737-8833-4f62-b670-fc1d41193b18\", \"contactNumber\": \"+48123456789\", \"emailAddress\": \"contact@company.com\"}, \"defaultDeliveryAddress\": {\"addressId\": \"da029d1a-b8c8-4d56-a930-5753b3b49c16\", \"street\": \"Warszawska 12\", \"city\": \"Warszawa\", \"postalCode\": \"00-001\", \"country\": \"Polska\"}}")
        Optional<CustomerDTO> customer,

        @Schema(description = "Adres wysyłki zamówienia", nullable = true,
                example = "{\"addressId\": \"da029d1a-b8c8-4d56-a930-5753b3b49c16\", \"street\": \"Warszawska 12\", \"city\": \"Warszawa\", \"postalCode\": \"00-001\", \"country\": \"Polska\"}")
        Optional<AddressDTO> address,

        @Schema(description = "Identyfikator faktury powiązanej z zamówieniem", nullable = true, example = "c7b87db6-bf19-4e36-8c77-8fa6d9b4b8b3")
        Optional<UUID> invoiceId,

        @Schema(description = "Lista pozycji zamówienia",
                example = "[{\"productOrderItemId\": \"c8cbd1c4-43e6-4672-94e7-89f5c0139a52\", \"productId\": \"b5acfd4e-d57a-46ac-a800-e9a2eb2350ab\", \"amount\": 2, \"unitPrice\": 2999.99}, {\"productOrderItemId\": \"edba0c1f-d7c9-45d5-9b6b-e6f7099f9e5f\", \"productId\": \"4f7eddd1-0f7a-47a2-b0d9-46f0296d1a6a\", \"amount\": 1, \"unitPrice\": 1199.99}]")
        List<ProductOrderItemDetailsDTO> productOrderItems,

        @Schema(description = "Całkowita kwota do zapłaty za zamówienie", example = "6499.97")
        BigDecimal totalDue,

        @Schema(description = "Numer śledzenia przesyłki", nullable = true, example = "TRACK123456")
        Optional<String> trackingNumber,

        @Schema(description = "Czas wysyłki zamówienia", nullable = true, example = "2024-11-14T15:30:00")
        Optional<LocalDateTime> timeOfSending,

        @Schema(description = "Czas utworzenia zamówienia", example = "2024-11-14T09:00:00")
        LocalDateTime timeOfCreation
) {

    public static ProductOrderDetailsDTO toProductOrderDetailsDTO(ProductOrder productOrder) {
        return new ProductOrderDetailsDTO(
                productOrder.getProductOrderId(),
                productOrder.getStatus(),
                Optional.ofNullable(productOrder.getCustomer())
                        .map(CustomerDTO::toCustomerDTO),
                Optional.ofNullable(productOrder.getAddress())
                        .map(AddressDTO::toAddressDTO),
                Optional.ofNullable(productOrder.getInvoice() != null ? productOrder.getInvoice().getInvoiceId() : null),
                Optional.ofNullable(productOrder.getOrderItems())
                        .map(items -> items.stream()
                                .map(ProductOrderItemDetailsDTO::toProductOrderItemDetailsDTO)
                                .toList())
                        .orElse(List.of()),
                productOrder.getTotalDue(),
                Optional.ofNullable(productOrder.getTrackingNumber()),
                Optional.ofNullable(productOrder.getTimeOfSending()),
                productOrder.getTimeOfCreation()
        );
    }
}



