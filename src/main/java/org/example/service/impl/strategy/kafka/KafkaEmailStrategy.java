package org.example.service.impl.strategy.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.events.EmailEvent;
import org.example.entity.ConfirmationToken;
import org.example.exception.errors.EntityNotFoundException;
import org.example.exception.errors.ExceptionDetection;
import org.example.repository.ConfirmationTokenRepository;
import org.example.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.email.retry.mode",
        havingValue = "kafka",
        matchIfMissing = false
)
public class KafkaEmailStrategy {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;
    private final ExceptionDetection exceptionDetection;
    private final KafkaTemplate<String, EmailEvent> kafkaTemplate;
    @Value("${app.email.retry.max-attempts}")
    private Integer count;
    @Value("${app.email.kafka.topic-name-retry}")
    private String emailRetryTopic;
    @Value("${app.email.kafka.topic-name-exception}")
    private String emailExceptionTopic;

    @KafkaListener(topics = "${app.email.kafka.topic-name}", groupId = "group1")
    public void emailService(EmailEvent emailEvent) throws InterruptedException {
        try {
            emailService.checkSendEmail(emailEvent);
        } catch (Throwable ex) {
            if (exceptionDetection.checkRetentionException(ex)) {
                emailEvent.setAttempt(emailEvent.getAttempt() + 1);
                if (emailEvent.getAttempt() >= count) {
                    emailEvent.setEx(ex);
                    kafkaTemplate.send(emailExceptionTopic,
                            emailEvent);
                    log.error("Email not send " + ex);
                    return;
                }
                kafkaTemplate.send(emailRetryTopic, emailEvent);
            } else {
                emailEvent.setEx(ex);
                kafkaTemplate.send(emailExceptionTopic,
                        emailEvent);
                log.error("Email not send, no retry exception " + ex);
                return;
            }
        }
    }

    @KafkaListener(topics = "${app.email.kafka.topic-name-retry}",
                    groupId = "group1")
    public void emailServiceRetry(EmailEvent emailEvent) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findById(emailEvent.getIdToken())
                .orElseThrow(() -> new EntityNotFoundException("Confirmation token is not found"));
        confirmationTokenRepository.incrementCountRetry(
                emailEvent.getIdToken(),
                LocalDateTime.now(),
                emailEvent.getEx() == null ? "Exception" : emailEvent.getEx().getMessage()
        );
        emailService.checkSendEmail(emailEvent);
    }

    @KafkaListener(topics = "${app.email.kafka.topic-name-exception}",
                    groupId = "group1")
    public void emailServiceException(EmailEvent emailEvent) {
        confirmationTokenRepository.markAsError(
                emailEvent.getIdToken(),
                emailEvent.getEx().getMessage()
        );
    }
}
