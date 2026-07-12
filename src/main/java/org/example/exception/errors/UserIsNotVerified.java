package org.example.exception.errors;

public class UserIsNotVerified extends RuntimeException {
    public UserIsNotVerified(String message) {
        super(message);
    }
}
