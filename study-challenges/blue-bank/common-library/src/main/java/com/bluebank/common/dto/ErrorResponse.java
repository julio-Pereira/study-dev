package com.bluebank.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard error response format for REST APIs.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;

    @Builder.Default
    private List<ValidationError> errors = new ArrayList<>();

    /**
     * Represents a validation error.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }

    /**
     * Factory method for a simple error response.
     *
     * @param errorCode the error code
     * @param message the error message
     * @param status the HTTP status code
     * @param path the request path
     * @return an error response
     */
    public static ErrorResponse of(String errorCode, String message, int status, String path) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Adds a validation error to the response.
     *
     * @param field the field name
     * @param message the error message
     */
    public void addValidationError(String field, String message) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ValidationError(field, message));
    }
}
