package org.example.service.impl.strategy;

import org.example.dto.events.EmailEvent;
import org.example.entity.ConfirmationToken;
import org.example.entity.Users;

public interface EmailSendStrategy {
    void sendEmail(Users user);
}
