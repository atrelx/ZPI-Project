package com.zpi.amoz.requests;

import com.zpi.amoz.models.ContactPerson;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record CustomerCreateRequest(
        @NotNull(message = "Contact person details must not be null")
        ContactPersonCreateRequest contactPerson,

        Optional<AddressCreateRequest> defaultDeliveryAddress
) {
}
