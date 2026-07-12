package org.example.exception.errors;

public class IncorrectFileFormatException extends RuntimeException {
    public IncorrectFileFormatException(String message) {
        super(message);
    }
}
