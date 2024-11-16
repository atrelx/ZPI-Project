package com.zpi.amoz.dtos;

import com.zpi.amoz.models.CustomerB2B;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO reprezentujące dane klienta B2B, zawierające dane klienta, adres oraz informacje dotyczące fakturowania.")
public record CustomerB2BDTO(

        @Schema(description = "Informacje o kliencie", example = "{ \"customerId\": \"f7593b1d-b85f-43c1-b876-61fc0c075123\", \"contactPerson\": { \"contactPersonId\": \"a47bc8f1-149f-4a5b-90f9-e253f70db681\", \"name\": \"Jan Kowalski\", \"email\": \"jan.kowalski@example.com\", \"phone\": \"123-456-789\" }, \"defaultDeliveryAddress\": { \"addressId\": \"123e4567-e89b-12d3-a456-426614174000\", \"city\": \"Warszawa\", \"street\": \"Ul. Pięciomorgowa\", \"streetNumber\": \"22\", \"apartmentNumber\": \"5A\", \"postalCode\": \"00-123\", \"additionalInformation\": \"Zamieszkuję na piętrze\" } }")
        CustomerDTO customer,

        @Schema(description = "Adres klienta", implementation = AddressDTO.class)
        AddressDTO address,

        @Schema(description = "Nazwa na fakturze", example = "Firma XYZ Sp. z o.o.")
        String nameOnInvoice,

        @Schema(description = "Numer firmy (np. NIP)", example = "1234567890")
        String companyNumber
) {
    public static CustomerB2BDTO toCustomerB2BDTO(CustomerB2B customerB2B) {
        return new CustomerB2BDTO(
                CustomerDTO.toCustomerDTO(customerB2B.getCustomer()),
                AddressDTO.toAddressDTO(customerB2B.getAddress()),
                customerB2B.getNameOnInvoice(),
                customerB2B.getCompanyNumber()
        );
    }
}




