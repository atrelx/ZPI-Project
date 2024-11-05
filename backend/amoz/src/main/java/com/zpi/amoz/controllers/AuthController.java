package com.zpi.amoz.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.zpi.amoz.responses.AuthTokenResponse;
import com.zpi.amoz.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${spring.security.oauth2.resource-server.opaque-token.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resource-server.opaque-token.client-secret}")
    private String clientSecret;

    @Autowired
    private OpaqueTokenIntrospector introspector;

    @Autowired
    private UserService userService;

    @GetMapping("/token")
    public ResponseEntity<AuthTokenResponse> getTokens(@RequestParam String authCode) {
        GoogleTokenResponse tokenResponse;
        String userId;
        try {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(), new GsonFactory(),
                    clientId,
                    clientSecret,
                    authCode,
                    ""
            ).execute();


        } catch (IOException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthTokenResponse response = new AuthTokenResponse(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthTokenResponse> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    refreshToken,
                    clientId,
                    clientSecret
            ).execute();

            AuthTokenResponse response = new AuthTokenResponse(
                    tokenResponse.getAccessToken(),
                    refreshToken
            );

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
