package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.Sex;
import com.zpi.amoz.models.Person;

import java.time.LocalDate;
import java.util.UUID;

public record PersonDTO(
        UUID personId,
        String name,
        String surname,
        LocalDate dateOfBirth,
        Sex sex
) {
    public static PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(
                person.getPersonId(),
                person.getName(),
                person.getSurname(),
                person.getDateOfBirth(),
                person.getSex()
        );
    }
}
