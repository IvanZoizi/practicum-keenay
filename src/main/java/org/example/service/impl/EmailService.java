package org.example.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.events.EmailEvent;
import org.example.entity.ConfirmationToken;
import org.example.exception.errors.*;
import org.example.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ExceptionDetection exceptionDetection;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    private void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Simple email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void checkSendEmail(EmailEvent emailEvent) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findById(emailEvent.getIdToken())
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));
        if (confirmationToken.getIsShipped()) {
            throw new MailHasBeenSentException("The mail has already been sent");
        }
        if (confirmationToken.getIsError()) {
            throw new EmailDoNotSendException("The mail does not send");
        }
        if (confirmationToken.getUser().getEnabled()) {
            throw new UserIsVerifiedException("User is already verified");
        }

        sendSimpleEmail(
                emailEvent.getEmail(),
                "Подтверждение регистрации",
                "Привет! Перейди по ссылке: http://127.0.0.1:8000/api/v1/auth/accept?token=" + confirmationToken.getToken()
        );

        confirmationToken.setIsShipped(true);
        confirmationTokenRepository.save(confirmationToken);
    }
}