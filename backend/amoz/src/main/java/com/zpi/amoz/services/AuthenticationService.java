package com.zpi.amoz.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.zpi.amoz.responses.AuthTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenticationService {

    @Value("${spring.security.oauth2.resource-server.opaque-token.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resource-server.opaque-token.client-secret}")
    private String clientSecret;

    public AuthTokenResponse getTokens(String authCode) {
        GoogleTokenResponse tokenResponse;
        try {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(), new GsonFactory(),
                    clientId,
                    clientSecret,
                    authCode,
                    ""
            ).execute();

            return new AuthTokenResponse(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
        } catch (IOException e) {
            throw new RuntimeException("Failed to exchange authorization code for tokens", e);
        }
    }

    public AuthTokenResponse refreshAccessToken(String refreshToken) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    refreshToken,
                    clientId,
                    clientSecret
            ).execute();

            return new AuthTokenResponse(
                    tokenResponse.getAccessToken(),
                    refreshToken
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to refresh the access token", e);
        }
    }
}
