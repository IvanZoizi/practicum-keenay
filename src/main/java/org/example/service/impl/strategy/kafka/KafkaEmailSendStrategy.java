package org.example.service.impl.strategy.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.events.EmailEvent;
import org.example.entity.ConfirmationToken;
import org.example.entity.Users;
import org.example.exception.errors.EntityNotFoundException;
import org.example.repository.ConfirmationTokenRepository;
import org.example.repository.UsersRepository;
import org.example.service.impl.strategy.EmailSendStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.email.retry.mode",
        havingValue = "kafka",
        matchIfMissing = false
)
public class KafkaEmailSendStrategy implements EmailSendStrategy {

    private final KafkaTemplate<String, EmailEvent> kafkaTemplateEmail;
    private final UsersRepository usersRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Value("${app.email.kafka.topic-name}")
    private String topicName;

    @Override
    public void sendEmail(Users user) {

        ConfirmationToken confirmationToken = new ConfirmationToken();

        String tokenValue = UUID.randomUUID().toString();
        LocalDateTime date = LocalDateTime.now().plusDays(7);

        confirmationToken.setUser(user);
        confirmationToken.setToken(tokenValue);
        confirmationToken.setExpiresAt(date);

        EmailEvent emailEvent = new EmailEvent();
        emailEvent.setIdToken(confirmationTokenRepository.save(confirmationToken).getId());
        emailEvent.setEmail(usersRepository.findEmailByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User is not found")));
        emailEvent.setAttempt(0);

        kafkaTemplateEmail.send(
                topicName,
                emailEvent
        );

    }
}
