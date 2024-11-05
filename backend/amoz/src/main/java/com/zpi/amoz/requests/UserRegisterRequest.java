package com.zpi.amoz.requests;

import com.zpi.amoz.enums.Sex;
import com.zpi.amoz.models.Person;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Optional;

public record UserRegisterRequest(
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
        Sex sex,

        @NotBlank(message = "Contact number is required.")
        String contactNumber,

        Optional<@Email(message = "Email must be valid.") String> emailAddress
) {
}