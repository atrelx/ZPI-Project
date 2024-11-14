package com.zpi.amoz.dtos;

import com.zpi.amoz.enums.Sex;
import com.zpi.amoz.models.Person;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "DTO reprezentujące osobę w systemie, zawierające podstawowe dane osobowe.")
public record PersonDTO(

        @Schema(description = "Identyfikator osoby", example = "1f3d4d5e-3c11-4e53-8f82-e2f0d8c94c89")
        UUID personId,

        @Schema(description = "Imię osoby", example = "Jan")
        String name,

        @Schema(description = "Nazwisko osoby", example = "Kowalski")
        String surname,

        @Schema(description = "Data urodzenia osoby", example = "1985-04-23")
        LocalDate dateOfBirth,

        @Schema(description = "Płeć osoby", example = "M")
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


