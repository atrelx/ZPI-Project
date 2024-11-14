package com.zpi.amoz.requests;

import com.zpi.amoz.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Request do tworzenia danych osobowych, zawierający imię, nazwisko, datę urodzenia i płeć.")
public record PersonCreateRequest(

        @Schema(description = "Imię osoby", example = "Jan", required = true)
        @NotBlank(message = "Name is required.")
        @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters.")
        String name,

        @Schema(description = "Nazwisko osoby", example = "Kowalski", required = true)
        @NotBlank(message = "Surname is required.")
        @Size(min = 2, max = 30, message = "Surname must be between 2 and 30 characters.")
        String surname,

        @Schema(description = "Data urodzenia osoby", example = "1990-05-15", required = true)
        @NotNull(message = "Date of birth is required.")
        @Past(message = "Date of birth must be a date in the past.")
        LocalDate dateOfBirth,

        @Schema(description = "Płeć osoby", example = "M", required = true)
        @NotNull(message = "Sex is required.")
        Sex sex
) {
}
