package org.example.exception.errors;

public class AmountOfDataError extends RuntimeException {
    public AmountOfDataError(String message) {
        super(message);
    }
}
