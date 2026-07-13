package org.example.exception.errors;

public class FileIsTooBigException extends RuntimeException {
    public FileIsTooBigException(String message) {
        super(message);
    }
}
