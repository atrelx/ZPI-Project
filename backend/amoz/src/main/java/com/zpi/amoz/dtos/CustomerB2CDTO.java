package com.zpi.amoz.dtos;

import com.zpi.amoz.models.CustomerB2C;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO reprezentujące dane klienta B2C, zawierające dane klienta oraz dane osoby kontaktowej.")
public record CustomerB2CDTO(

        @Schema(description = "Informacje o kliencie", implementation = CustomerDTO.class)
        CustomerDTO customer,

        @Schema(description = "Informacje o osobie kontaktowej", implementation = PersonDTO.class)
        PersonDTO person
) {
    public static CustomerB2CDTO toCustomerB2CDTO(CustomerB2C customerB2C) {
        return new CustomerB2CDTO(
                CustomerDTO.toCustomerDTO(customerB2C.getCustomer()),
                PersonDTO.toPersonDTO(customerB2C.getPerson())
        );
    }
}