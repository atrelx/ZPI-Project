package com.zpi.amoz.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.zpi.amoz.requests.PushRequest;
import org.springframework.stereotype.Service;

@Service
public class PushService {
    public String sendMessage(PushRequest request) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(request.targetToken())
                .setNotification(Notification.builder()
                        .setTitle(request.title())
                        .setBody(request.body())
                        .build())
                .putData("deeplink", request.deeplink().orElse(null))
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }
}