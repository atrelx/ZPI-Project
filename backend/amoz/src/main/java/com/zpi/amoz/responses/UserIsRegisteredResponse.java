package com.zpi.amoz.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response wskazujący, czy użytkownik jest zarejestrowany w systemie.")
public record UserIsRegisteredResponse(

        @Schema(description = "Wartość wskazująca, czy użytkownik jest zarejestrowany (true/false).", example = "true")
        Boolean isRegistered

) {
}
