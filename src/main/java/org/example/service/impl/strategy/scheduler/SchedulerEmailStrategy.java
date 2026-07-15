package org.example.service.impl.strategy.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.events.EmailEvent;
import org.example.entity.ConfirmationToken;
import org.example.exception.errors.EntityNotFoundException;
import org.example.exception.errors.ExceptionDetection;
import org.example.repository.ConfirmationTokenRepository;
import org.example.repository.UsersRepository;
import org.example.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.email.retry.mode",
        havingValue = "worker",
        matchIfMissing = false
)
public class SchedulerEmailStrategy {

    private final EmailService emailService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ExceptionDetection exceptionDetection;
    private final UsersRepository usersRepository;

    @Value("${app.email.retry.max-attempts:3}")
    private Integer maxAttempts;

    @Value("${app.email.retry.worker.batch:10}")
    private Integer batchSize;

    @Scheduled(
            fixedDelayString = "${app.email.retry.worker.fixed-delay:30000}",
            initialDelay = 5000
    )
    @Transactional
    public void checkTokens() {
        LocalDateTime now = LocalDateTime.now();
        try {
            List<ConfirmationToken> pendingTokens = confirmationTokenRepository
                    .findPendingTokensWithRetryLimit(now, maxAttempts);

            if (pendingTokens.isEmpty()) {
                return;
            }

            List<ConfirmationToken> batch = pendingTokens.stream()
                    .limit(batchSize)
                    .toList();

            for (ConfirmationToken token : batch) {
                try {
                    EmailEvent event = new EmailEvent();
                    event.setIdToken(token.getId());
                    event.setAttempt(0);
                    event.setEmail(usersRepository.findEmailByUserId(token.getUser().getId())
                            .orElseThrow(() -> new EntityNotFoundException("Email is not found")));

                    emailService.checkSendEmail(event);

                } catch (Exception e) {

                    if (exceptionDetection.checkRetentionException(e)) {
                        confirmationTokenRepository.incrementCountRetry(
                                token.getId(),
                                LocalDateTime.now(),
                                e.getMessage()
                        );
                    } else {
                        confirmationTokenRepository.markAsError(
                                token.getId(),
                                e.getMessage()
                        );
                    }
                }
            }
        } catch (Exception e) {
            log.error("Worker scheduler error: {}", e.getMessage(), e);
            throw new RuntimeException("Worker does not send email " + e.getMessage());
        }
    }
}