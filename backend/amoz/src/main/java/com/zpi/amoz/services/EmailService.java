package com.zpi.amoz.services;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.*;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Base64;
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

    @Async
    public CompletableFuture<Boolean> sendEmailWithAttachment(List<String> toEmailList,
                                                              String subject,
                                                              String htmlContent,
                                                              byte[] pdfBytes,
                                                              String fileName) {
        List<EmailAddress> toEmailAddressList = toEmailList.stream()
                .map(EmailAddress::new).toList();

        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

        BinaryData binaryData = BinaryData.fromString(base64Pdf);

        EmailAttachment attachment = new EmailAttachment(fileName, "application/pdf", binaryData);

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setSenderAddress(senderEmailAddress);
        emailMessage.setSubject(subject);
        emailMessage.setToRecipients(toEmailAddressList);
        emailMessage.setBodyHtml(htmlContent);
        emailMessage.setAttachments(List.of(attachment));

        SyncPoller<EmailSendResult, EmailSendResult> syncPoller = emailClient.beginSend(emailMessage);
        syncPoller.waitForCompletion();

        EmailSendResult result = syncPoller.getFinalResult();
        if (result.getStatus() == EmailSendStatus.SUCCEEDED) {
            System.out.println("Email with attachment sent successfully.");
            return CompletableFuture.completedFuture(true);
        } else {
            System.out.println("Failed to send email with attachment.");
            return CompletableFuture.completedFuture(false);
        }
    }
}
