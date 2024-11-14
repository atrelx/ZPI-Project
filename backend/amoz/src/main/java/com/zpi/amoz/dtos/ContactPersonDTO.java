package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ContactPerson;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO reprezentujące dane kontaktowe osoby kontaktowej.")
public record ContactPersonDTO(

        @Schema(description = "ID osoby kontaktowej", example = "e4b5fa0f-b8b1-4b60-bb6b-e4b3d91811ed")
        UUID contactPersonId,

        @Schema(description = "Numer kontaktowy osoby kontaktowej", example = "+48123456789")
        String contactNumber,

        @Schema(description = "Adres e-mail osoby kontaktowej", example = "contact@company.com")
        String emailAddress
) {
    public static ContactPersonDTO toContactPersonDTO(ContactPerson contactPerson) {
        return new ContactPersonDTO(
                contactPerson.getContactPersonId(),
                contactPerson.getContactNumber(),
                contactPerson.getEmailAddress()
        );
    }
}