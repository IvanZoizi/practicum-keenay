package org.example.exception.errors;

public class MailIsBusyException extends RuntimeException {
    public MailIsBusyException(String message) {
        super(message);
    }
}
