package com.zpi.amoz.controllers;

import com.zpi.amoz.responses.AuthTokenResponse;
import com.zpi.amoz.responses.MessageResponse;
import com.zpi.amoz.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "Pobierz tokeny", description = "Pobiera tokeny dostępu na podstawie kodu autoryzacyjnego.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano tokeny",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokenResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Nieautoryzowany - nieprawidłowy kod autoryzacyjny",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @GetMapping("/token")
    public ResponseEntity<?> getTokens(@RequestParam String authCode) {
        try {
            AuthTokenResponse response = authenticationService.getTokens(authCode);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Odśwież token dostępu", description = "Odświeża token dostępu na podstawie tokena odświeżania.")
    @ApiResponse(responseCode = "200", description = "Pomyślnie odświeżono token",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokenResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Nieautoryzowany - nieprawidłowy token odświeżania",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
    )
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            AuthTokenResponse response = authenticationService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(e.getMessage()));
        }
    }
}

