package org.example.exception;

import jakarta.mail.MessagingException;
import org.example.exception.errors.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> createMessage(Throwable ex) {
        Map<String, Object> body = new HashMap<>();

        String message = ex.getMessage();
        if (message == null || message.isEmpty()) {
            message = ex.getClass().getSimpleName() + " occurred";
        }

        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("exception", ex.getClass().getSimpleName());

        ex.printStackTrace();

        return body;
    }

    @ExceptionHandler(EmailDoNotSendException.class)
    public ResponseEntity<Object> handleEmailDoNotSendException(EmailDoNotSendException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MailHasBeenSentException.class)
    public ResponseEntity<Object> handleMailHasBeenSentException(MailHasBeenSentException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsVerifiedException.class)
    public ResponseEntity<Object> handleUserIsVerifiedException44(UserIsVerifiedException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenAliveException.class)
    public ResponseEntity<Object> handleTokenAliveException(TokenAliveException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenDyingException.class)
    public ResponseEntity<Object> handleTokenDyingException(TokenDyingException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsNotVerified.class)
    public ResponseEntity<Object> handleUserIsNotVerified(UserIsNotVerified ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<Object> handleTokenNotFoundException(TokenNotFoundException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<Object> handleFileException(FileException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IncorrectFileFormatException.class)
    public ResponseEntity<Object> handleIncorrectFileFormatException(IncorrectFileFormatException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AmountOfDataError.class)
    public ResponseEntity<Object> handleAmountOfDataError(AmountOfDataError ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        Map<String, Object> body = createMessage(ex);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}