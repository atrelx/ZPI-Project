package com.zpi.amoz.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerB2BCreateRequest(
        @NotNull(message = "Customer section must be filled")
        CustomerCreateRequest customer,

        @NotEmpty(message = "Company number must not be empty")
        @Size(max = 30)
        String companyNumber,

        @NotEmpty(message = "Name on invoice must not be empty")
        @Size(max = 255)
        String nameOnInvoice,

        @NotNull(message = "Company's address must be given")
        AddressCreateRequest address
) {
}