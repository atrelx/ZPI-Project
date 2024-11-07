package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.Customer;

import java.util.Optional;
import java.util.UUID;

import static com.zpi.amoz.dtos.AddressDTO.toAddressDTO;


public record CustomerDTO(
        UUID customerId,
        ContactPersonDTO contactPerson,
        String nameOnInvoice,
        Optional<AddressDTO> defaultDeliveryAddress
) {
    static CustomerDTO toCustomerDTO(Customer customer) {
        return new CustomerDTO(
                customer.getCustomerId(),
                ContactPersonDTO.toContactPersonDTO(customer.getContactPerson()),
                customer.getNameOnInvoice(),
                Optional.ofNullable(customer.getDefaultDeliveryAddress() != null ? AddressDTO.toAddressDTO(customer.getDefaultDeliveryAddress()) : null)
        );
    }
}