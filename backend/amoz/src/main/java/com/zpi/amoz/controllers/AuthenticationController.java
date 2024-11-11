package com.zpi.amoz.controllers;

import com.zpi.amoz.responses.AuthTokenResponse;
import com.zpi.amoz.services.AuthenticationService;
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

    @GetMapping("/token")
    public ResponseEntity<AuthTokenResponse> getTokens(@RequestParam String authCode) {
        try {
            AuthTokenResponse response = authenticationService.getTokens(authCode);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthTokenResponse> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            AuthTokenResponse response = authenticationService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
