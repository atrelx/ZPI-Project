package com.zpi.amoz.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

@Schema(description = "Request do tworzenia osoby kontaktowej, zawierający numer kontaktowy oraz opcjonalny adres e-mail.")
public record ContactPersonCreateRequest(

        @Schema(description = "Numer kontaktowy osoby", example = "+48123456789")
        @NotBlank(message = "Contact number is required.")
        String contactNumber,

        @Schema(description = "Adres e-mail osoby kontaktowej (opcjonalny)", example = "contact@company.com", nullable = true)
        Optional<@Email(message = "Email must be valid.") String> emailAddress

) {
}
