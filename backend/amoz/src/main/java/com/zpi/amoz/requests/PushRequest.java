package com.zpi.amoz.requests;

import java.util.Optional;

public record PushRequest(
        String targetToken,
        String title,
        String body,
        Optional<String> deeplink
) {
}
