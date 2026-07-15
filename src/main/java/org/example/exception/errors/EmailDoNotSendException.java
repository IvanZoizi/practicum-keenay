package org.example.exception.errors;

public class EmailDoNotSendException extends RuntimeException {
    public EmailDoNotSendException(String message) {
        super(message);
    }
}
