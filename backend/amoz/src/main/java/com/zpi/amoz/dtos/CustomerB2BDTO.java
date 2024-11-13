package com.zpi.amoz.dtos;

import com.zpi.amoz.models.CustomerB2B;

import java.util.UUID;

public record CustomerB2BDTO(
        CustomerDTO customer,
        AddressDTO address,
        String nameOnInvoice,
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

