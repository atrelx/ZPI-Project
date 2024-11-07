package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;
import java.util.UUID;


public record AddressDTO(
        UUID addressId,
        String city,
        String street,
        String streetNumber,
        String apartmentNumber,
        String postalCode,
        Optional<String> additionalInformation
) {
    static AddressDTO toAddressDTO(Address address) {
        return new AddressDTO(
                address.getAddressId(),
                address.getCity(),
                address.getStreet(),
                address.getStreetNumber(),
                address.getApartmentNumber(),
                address.getPostalCode(),
                Optional.ofNullable(address.getAdditionalInformation())
        );
    }
}