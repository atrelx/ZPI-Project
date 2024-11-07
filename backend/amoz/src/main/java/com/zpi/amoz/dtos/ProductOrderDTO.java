package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.Status;
import com.zpi.amoz.models.ProductOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ProductOrderDTO(UUID productOrderId,
                              Status status,
                              Optional<CustomerDTO> customer,
                              Optional<AddressDTO> address,
                              Optional<InvoiceDTO> invoice,
                              List<ProductOrderItemDTO> productOrderItems,
                              Optional<String> trackingNumber,
                              Optional<LocalDateTime> timeOfSending,
                              Optional<LocalDateTime> timeOfCreation) {

    public static ProductOrderDTO toProductOrderDTO(ProductOrder productOrder) {
        return new ProductOrderDTO(
                productOrder.getProductOrderId(),
                productOrder.getStatus(),
                Optional.ofNullable(productOrder.getCustomer() != null ? CustomerDTO.toCustomerDTO(productOrder.getCustomer()) : null),
                Optional.ofNullable(productOrder.getAddress() != null ? AddressDTO.toAddressDTO(productOrder.getAddress()) : null),
                Optional.ofNullable(productOrder.getInvoice() != null ? InvoiceDTO.toInvoiceDTO(productOrder.getInvoice()) : null),
                productOrder.getOrderItems() != null ? productOrder.getOrderItems().stream()
                        .map(ProductOrderItemDTO::toProductOrderItemDTO).toList() : List.of(),
                Optional.ofNullable(productOrder.getTrackingNumber()),
                Optional.ofNullable(productOrder.getTimeOfSending()),
                Optional.ofNullable(productOrder.getTimeOfCreation())
        );
    }
}
