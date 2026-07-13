package org.example.exception.errors;

public class MailHasBeenSentException extends RuntimeException {
    public MailHasBeenSentException(String message) {
        super(message);
    }
}
