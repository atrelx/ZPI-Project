package com.zpi.amoz.services;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailMessage;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.communication.email.models.EmailSendStatus;
import com.azure.core.util.polling.SyncPoller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    private final String connectionString;

    private final String senderEmailAddress;

    private final EmailClient emailClient;

    @Autowired
    public EmailService(@Value("${azure.communication.connection-string}") String connectionString,
                        @Value("${azure.communication.sender-email-address}") String senderEmailAddress) {
        this.connectionString = connectionString;
        this.senderEmailAddress = senderEmailAddress;

        this.emailClient = new EmailClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    @Async
    public CompletableFuture<Boolean> sendEmail(List<String> toEmailList, String subject, String htmlContent) {
        List<EmailAddress> toEmailAddressList = toEmailList.stream()
                .map(EmailAddress::new).toList();

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setSenderAddress(senderEmailAddress);
        emailMessage.setSubject(subject);
        emailMessage.setToRecipients(toEmailAddressList);
        emailMessage.setBodyHtml(htmlContent);

        SyncPoller<EmailSendResult, EmailSendResult> syncPoller = emailClient.beginSend(emailMessage);

        syncPoller.waitForCompletion();

        EmailSendResult result = syncPoller.getFinalResult();
        if (result.getStatus() == EmailSendStatus.SUCCEEDED) {
            System.out.println("Email sent successfully.");
            return CompletableFuture.completedFuture(true);
        } else {
            System.out.println("Failed to send email.");
            return CompletableFuture.completedFuture(false);
        }
    }
}
