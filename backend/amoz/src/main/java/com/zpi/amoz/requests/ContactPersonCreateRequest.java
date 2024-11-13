package com.zpi.amoz.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record ContactPersonCreateRequest(
        @NotBlank(message = "Contact number is required.")
        String contactNumber,

        Optional<@Email(message = "Email must be valid.") String> emailAddress
) {
}
