package com.zpi.amoz.dtos;

import com.zpi.amoz.models.CustomerB2C;

import java.util.UUID;

public record CustomerB2CDTO(
        UUID customerB2CID,
        CustomerDTO customer,
        PersonDTO person
) {
    public static CustomerB2CDTO toCustomerB2CDTO(CustomerB2C customerB2C) {
        return new CustomerB2CDTO(
                customerB2C.getCustomerB2CID(),
                customerB2C.getCustomer() != null ? CustomerDTO.toCustomerDTO(customerB2C.getCustomer()) : null,
                customerB2C.getPerson() != null ? PersonDTO.toPersonDTO(customerB2C.getPerson()) : null  // Przekazujemy PersonDTO
        );
    }
}