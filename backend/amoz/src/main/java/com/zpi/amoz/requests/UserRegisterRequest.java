package com.zpi.amoz.requests;

import com.zpi.amoz.enums.Sex;
import com.zpi.amoz.models.Person;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Optional;

@Schema(description = "Request do rejestracji użytkownika, zawierający dane osobowe oraz dane kontaktowe osoby kontaktowej.")
public record UserRegisterRequest(

        @Schema(description = "Dane osobowe użytkownika")
        @NotNull(message = "Personal information is required")
        PersonCreateRequest person,

        @Schema(description = "Dane kontaktowe osoby kontaktowej")
        @NotNull(message = "Contact person details are required")
        ContactPersonCreateRequest contactPerson
) {
}
