package org.example.service.impl.strategy.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.events.EmailEvent;
import org.example.entity.ConfirmationToken;
import org.example.entity.Users;
import org.example.repository.ConfirmationTokenRepository;
import org.example.service.impl.strategy.EmailSendStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.email.retry.mode",
        havingValue = "worker",
        matchIfMissing = false
)
public class SchedulerSendEmailStrategy implements EmailSendStrategy {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void sendEmail(Users user) {

        ConfirmationToken confirmationToken = new ConfirmationToken();

        String tokenValue = UUID.randomUUID().toString();
        LocalDateTime date = LocalDateTime.now().plusDays(7);

        confirmationToken.setUser(user);
        confirmationToken.setToken(tokenValue);
        confirmationToken.setExpiresAt(date);

        confirmationTokenRepository.save(confirmationToken);
    }
}
