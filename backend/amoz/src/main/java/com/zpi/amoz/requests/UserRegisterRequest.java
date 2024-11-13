package com.zpi.amoz.requests;

import com.zpi.amoz.enums.Sex;
import com.zpi.amoz.models.Person;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Optional;

public record UserRegisterRequest(
        @NotNull(message = "Personal information is required")
        PersonCreateRequest person,

        @NotNull(message = "Contact person details are required")
        ContactPersonCreateRequest contactPerson
) {
}