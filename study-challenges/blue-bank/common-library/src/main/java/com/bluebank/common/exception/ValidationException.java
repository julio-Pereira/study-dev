package com.bluebank.common.exception;

/**
 * Exception thrown when input validation fails.
 */
public class ValidationException extends BusinessException {
    private static final String ERROR_CODE = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(message, ERROR_CODE);
    }

    public ValidationException(String field, String message) {
        super(String.format("Field '%s': %s", field, message), ERROR_CODE);
    }
}