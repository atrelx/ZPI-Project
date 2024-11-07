package com.zpi.amoz.dtos;

import com.zpi.amoz.models.CustomerB2B;

import java.util.UUID;

public record CustomerB2BDTO(
        UUID customerB2BId,
        CustomerDTO customer,
        String addressOnInvoice,
        String companyNumber
) {
    public static CustomerB2BDTO toCustomerB2BDTO(CustomerB2B customerB2B) {
        return new CustomerB2BDTO(
                customerB2B.getCustomerB2BId(),
                customerB2B.getCustomer() != null ? CustomerDTO.toCustomerDTO(customerB2B.getCustomer()) : null,  // Przekazujemy CustomerDTO
                customerB2B.getAddressOnInvoice(),
                customerB2B.getCompanyNumber()
        );
    }
}

