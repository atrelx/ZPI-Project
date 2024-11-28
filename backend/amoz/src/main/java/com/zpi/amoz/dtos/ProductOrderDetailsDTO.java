package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.Status;
import com.zpi.amoz.models.ProductOrder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

        @Schema(description = "Informacje o kliencie składającym zamówienie", nullable = true, implementation = CustomerDTO.class)
        Optional<CustomerDTO> customer,

        @Schema(description = "Adres wysyłki zamówienia", nullable = true, implementation = AddressDTO.class)
        Optional<AddressDTO> address,

        @Schema(description = "Identyfikator faktury powiązanej z zamówieniem", nullable = true, example = "c7b87db6-bf19-4e36-8c77-8fa6d9b4b8b3")
        Optional<UUID> invoiceId,

        @ArraySchema(schema = @Schema(description = "Lista pozycji zamówienia", implementation = ProductOrderItemDetailsDTO.class))
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



