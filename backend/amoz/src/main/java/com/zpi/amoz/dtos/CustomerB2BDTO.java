package com.zpi.amoz.dtos;

import com.zpi.amoz.models.CustomerB2B;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO reprezentujące dane klienta B2B, zawierające dane klienta, adres oraz informacje dotyczące fakturowania.")
public record CustomerB2BDTO(

        @Schema(description = "Informacje o kliencie", implementation = CustomerDTO.class)
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




