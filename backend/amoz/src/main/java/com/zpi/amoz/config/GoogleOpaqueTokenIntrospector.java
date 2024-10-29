package com.zpi.amoz.config;
import com.zpi.amoz.dtos.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final WebClient userInfoClient;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        UserInfoDTO userInfoDTO = userInfoClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", token)
                        .build())
                .retrieve()
                .bodyToMono(UserInfoDTO.class)
                .block();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", userInfoDTO.sub());
        attributes.put("name", userInfoDTO.name());
        attributes.put("email", userInfoDTO.email());
        return new OAuth2IntrospectionAuthenticatedPrincipal(userInfoDTO.name(), attributes, null);
    }
}
