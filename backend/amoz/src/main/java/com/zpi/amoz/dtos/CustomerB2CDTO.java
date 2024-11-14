package com.zpi.amoz.dtos;

import com.zpi.amoz.models.CustomerB2C;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO reprezentujące dane klienta B2C, zawierające dane klienta oraz dane osoby kontaktowej.")
public record CustomerB2CDTO(

        @Schema(description = "Informacje o kliencie", example = "{ \"customerId\": \"f7593b1d-b85f-43c1-b876-61fc0c075123\", \"contactPerson\": { \"contactPersonId\": \"e4b5fa0f-b8b1-4b60-bb6b-e4b3d91811ed\", \"contactNumber\": \"+48123456789\", \"emailAddress\": \"contact@company.com\" }, \"defaultDeliveryAddress\": { \"addressId\": \"123e4567-e89b-12d3-a456-426614174000\", \"city\": \"Warszawa\", \"street\": \"Ul. Pięciomorgowa\", \"streetNumber\": \"22\", \"apartmentNumber\": \"5A\", \"postalCode\": \"00-123\", \"additionalInformation\": \"Zamieszkuję na piętrze\" } }")
        CustomerDTO customer,

        @Schema(description = "Informacje o osobie kontaktowej", example = "{ \"personId\": \"d4c5fba4-3a9e-401e-91e7-12546bba9c6c\", \"name\": \"Anna Nowak\", \"surname\": \"Nowak\", \"dateOfBirth\": \"1985-07-14\", \"sex\": \"F\" }")
        PersonDTO person
) {
    public static CustomerB2CDTO toCustomerB2CDTO(CustomerB2C customerB2C) {
        return new CustomerB2CDTO(
                CustomerDTO.toCustomerDTO(customerB2C.getCustomer()),
                PersonDTO.toPersonDTO(customerB2C.getPerson())
        );
    }
}



