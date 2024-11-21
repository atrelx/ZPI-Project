package com.zpi.amoz.requests;

import com.zpi.amoz.enums.Sex;
import com.zpi.amoz.models.Person;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Optional;

@Schema(description = "Request do rejestracji użytkownika, zawierający dane osobowe oraz dane kontaktowe osoby kontaktowej.")
public record UserRegisterRequest(

        @Schema(description = "Dane osobowe użytkownika", implementation = PersonCreateRequest.class)
        @NotNull(message = "Personal information is required")
        @Valid
        PersonCreateRequest person,

        @Schema(description = "Dane kontaktowe osoby kontaktowej", implementation = ContactPersonCreateRequest.class)
        @NotNull(message = "Contact person details are required")
        @Valid
        ContactPersonCreateRequest contactPerson
) {
}
