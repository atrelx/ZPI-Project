package com.zpi.amoz.requests;

import com.zpi.amoz.models.ContactPerson;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

@Schema(description = "Request do tworzenia klienta, zawierający dane osoby kontaktowej oraz opcjonalny domyślny adres dostawy.")
public record CustomerCreateRequest(

        @Schema(description = "Dane osoby kontaktowej dla klienta", implementation = ContactPersonCreateRequest.class)
        @NotNull(message = "Contact person details must not be null")
        @Valid
        ContactPersonCreateRequest contactPerson,

        @Schema(description = "Opcjonalny domyślny adres dostawy dla klienta", nullable = true, implementation = AddressCreateRequest.class)
        Optional<@Valid AddressCreateRequest> defaultDeliveryAddress

) {
}

