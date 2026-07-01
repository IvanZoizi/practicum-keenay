package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidValidator implements ConstraintValidator<EmailValid, String> {

    private static final Pattern SIMPLE_EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean isValidSimple(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return SIMPLE_EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return EmailValidValidator.isValidSimple(value);
    }
}