package com.zpi.amoz.services;

import com.google.firebase.messaging.*;
import com.zpi.amoz.requests.PushRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushService {
    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public String sendMessage(String pushToken, PushRequest request) throws FirebaseMessagingException {
        Message.Builder builder = Message.builder()
                .setToken(pushToken)
                .putData("title", request.title())
                .putData("body", request.body());
        if (request.deeplink().isPresent()) {
            builder = builder.putData("deeplink", request.deeplink().get());
        }

        return firebaseMessaging.send(builder.build());
    }

    public void sendBulkPushMessages(List<String> pushTokens, PushRequest request) {
        final int MAX_TOKENS = 500;
        for (int i = 0; i < pushTokens.size(); i += MAX_TOKENS) {
            List<String> tokens = pushTokens.subList(i, Math.min(i + MAX_TOKENS, pushTokens.size()));

            MulticastMessage.Builder builder = MulticastMessage.builder()
                    .addAllTokens(tokens)
                    .putData("title", request.title())
                    .putData("body", request.body());

            if (request.deeplink().isPresent()) {
                builder = builder.putData("deeplink", request.deeplink().get());
            }

            try {
                BatchResponse response = firebaseMessaging.sendMulticast(builder.build());
                System.out.println("Sent message to " + response.getSuccessCount() + " recipients.");
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException("Push sending failed", e);
            }
        }
    }

}