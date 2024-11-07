package com.zpi.amoz.dtos;

import com.zpi.amoz.models.ContactPerson;

import java.util.UUID;

public record ContactPersonDTO(
        UUID contactPersonId,
        String contactNumber,
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