package com.sms.multitenantschool.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String recipientEmail, String verificationToken) {
        String subject = "Email Verification";
        String verificationLink = frontendUrl + "/verify?token=" + verificationToken;
        String message = "<p>Thank you for registering!</p>"
                + "<p>Please verify your email by clicking the link below:</p>"
                + "<p><a href=\"" + verificationLink + "\">Verify Email</a></p>"
                + "<p>If you did not request this, please ignore this email.</p>";

        sendEmail(recipientEmail, subject, message);
    }

    private void sendEmail(String recipientEmail, String subject, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(message, true); // Enable HTML content

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

