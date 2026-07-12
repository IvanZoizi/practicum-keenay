package org.example.exception.errors;

public class TokenAliveException extends RuntimeException {
    public TokenAliveException(String message) {
        super(message);
    }
}
