package org.example.exception.errors;

public class UserIsVerifiedException extends RuntimeException {
    public UserIsVerifiedException(String message) {
        super(message);
    }
}
