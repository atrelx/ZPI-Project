package com.zpi.amoz.requests;

import com.zpi.amoz.enums.Sex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PersonCreateRequest(
        @NotBlank(message = "Name is required.")
        @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters.")
        String name,

        @NotBlank(message = "Surname is required.")
        @Size(min = 2, max = 30, message = "Surname must be between 2 and 30 characters.")
        String surname,

        @NotNull(message = "Date of birth is required.")
        @Past(message = "Date of birth must be a date in the past.")
        LocalDate dateOfBirth,

        @NotNull(message = "Sex is required.")
        Sex sex
) {
}
