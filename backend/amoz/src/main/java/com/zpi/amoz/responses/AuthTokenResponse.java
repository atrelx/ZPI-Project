package com.zpi.amoz.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response zawierający token dostępu oraz token odświeżający dla użytkownika po udanej autentykacji.")
public record AuthTokenResponse(

        @Schema(description = "Token dostępu, używany do autentykacji w kolejnych zapytaniach.")
        String accessToken,

        @Schema(description = "Token odświeżający, używany do uzyskania nowego tokenu dostępu, gdy wygasa.")
        String refreshToken

) {
}
