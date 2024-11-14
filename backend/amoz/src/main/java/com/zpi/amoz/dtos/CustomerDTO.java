package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Company;
import com.zpi.amoz.models.Customer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;
import java.util.UUID;

import static com.zpi.amoz.dtos.AddressDTO.toAddressDTO;


@Schema(description = "DTO reprezentujące dane klienta, w tym dane kontaktowe i domyślny adres dostawy.")
public record CustomerDTO(

        @Schema(description = "Identyfikator klienta", example = "f7593b1d-b85f-43c1-b876-61fc0c075123")
        UUID customerId,

        @Schema(description = "Dane kontaktowe osoby odpowiedzialnej za kontakt z klientem",
                example = "{ \"contactPersonId\": \"e4b5fa0f-b8b1-4b60-bb6b-e4b3d91811ed\", \"contactNumber\": \"+48123456789\", \"emailAddress\": \"contact@company.com\" }")
        ContactPersonDTO contactPerson,

        @Schema(description = "Domyślny adres dostawy klienta, jeśli istnieje", nullable = true,
                example = "{ \"addressId\": \"123e4567-e89b-12d3-a456-426614174000\", \"city\": \"Warszawa\", \"street\": \"Ul. Pięciomorgowa\", \"streetNumber\": \"22\", \"apartmentNumber\": \"5A\", \"postalCode\": \"00-123\", \"additionalInformation\": \"Zamieszkuję na piętrze\" }")
        Optional<AddressDTO> defaultDeliveryAddress

) {

    static CustomerDTO toCustomerDTO(Customer customer) {
        return new CustomerDTO(
                customer.getCustomerId(),
                ContactPersonDTO.toContactPersonDTO(customer.getContactPerson()),
                Optional.ofNullable(customer.getDefaultDeliveryAddress() != null ? AddressDTO.toAddressDTO(customer.getDefaultDeliveryAddress()) : null)
        );
    }
}



