package com.zpi.amoz.dtos;

import com.zpi.amoz.models.CustomerB2C;

import java.util.UUID;

public record CustomerB2CDTO(
        CustomerDTO customer,
        PersonDTO person
) {
    public static CustomerB2CDTO toCustomerB2CDTO(CustomerB2C customerB2C) {
        return new CustomerB2CDTO(
                CustomerDTO.toCustomerDTO(customerB2C.getCustomer()),
                PersonDTO.toPersonDTO(customerB2C.getPerson())
        );
    }
}
