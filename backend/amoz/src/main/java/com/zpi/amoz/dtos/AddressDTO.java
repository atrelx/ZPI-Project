package com.zpi.amoz.dtos;

import com.zpi.amoz.models.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;
import java.util.UUID;


@Schema(description = "Obiekt zawierający dane adresowe")
public record AddressDTO(

        @Schema(description = "Unikalny identyfikator adresu", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID addressId,

        @Schema(description = "Kraj, w którym znajduje się adres", example = "Poland")
        String country,

        @Schema(description = "Miasto, w którym znajduje się adres", example = "Warszawa")
        String city,

        @Schema(description = "Ulica, na której znajduje się adres", example = "Ul. Pięciomorgowa")
        String street,

        @Schema(description = "Numer ulicy", example = "22")
        String streetNumber,

        @Schema(description = "Numer mieszkania (opcjonalnie)", example = "5A")
        Optional<String> apartmentNumber,

        @Schema(description = "Kod pocztowy", example = "00-123")
        String postalCode,

        @Schema(description = "Dodatkowe informacje dotyczące adresu", example = "Zamieszkuję na piętrze", nullable = true)
        Optional<String> additionalInformation
) {
    static AddressDTO toAddressDTO(Address address) {
        return new AddressDTO(
                address.getAddressId(),
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getStreetNumber(),
                Optional.ofNullable(address.getApartmentNumber()),
                address.getPostalCode(),
                Optional.ofNullable(address.getAdditionalInformation())
        );
    }
}
