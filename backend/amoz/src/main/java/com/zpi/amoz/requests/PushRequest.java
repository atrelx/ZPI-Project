package com.zpi.amoz.requests;

import java.util.Optional;

public record PushRequest(

        String title,
        String body,
        Optional<String> deeplink
) {
}
