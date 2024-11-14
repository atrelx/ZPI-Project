package com.zpi.amoz.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO reprezentujące dane użytkownika pochodzące z zewnętrznego źródła (np. OAuth), takie jak imię, nazwisko, adres e-mail i inne informacje.")
public record UserInfoDTO(

        @Schema(description = "Unikalny identyfikator użytkownika", example = "12345")
        String sub,

        @Schema(description = "Imię użytkownika", example = "Jan")
        String name,

        @Schema(description = "Imię użytkownika (second name)", example = "Kowalski")
        String given_name,

        @Schema(description = "Nazwisko użytkownika", example = "Kowalski")
        String family_name,

        @Schema(description = "URL do zdjęcia profilowego użytkownika", example = "https://example.com/user-profile.jpg")
        String picture,

        @Schema(description = "Adres e-mail użytkownika", example = "jan.kowalski@example.com")
        String email,

        @Schema(description = "Czy adres e-mail użytkownika jest zweryfikowany", example = "true")
        boolean email_verified,

        @Schema(description = "Język preferowany przez użytkownika", example = "pl")
        String locale

) {
}

