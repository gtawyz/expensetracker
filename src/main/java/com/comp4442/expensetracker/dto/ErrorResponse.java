package com.comp4442.expensetracker.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private Map<String, String> fieldErrors;
    private LocalDateTime timestamp;

    // Creates a basic error response with status, error label, message, and timestamp.
    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Creates an error response that also includes validation errors for individual fields.
    public ErrorResponse(int status, String error, String message, Map<String, String> fieldErrors) {
        this(status, error, message);
        this.fieldErrors = fieldErrors;
    }

    // Returns the HTTP status code for this error.
    public int getStatus() {
        return status;
    }

    // Returns the short error label.
    public String getError() {
        return error;
    }

    // Returns the detailed error message shown to the client.
    public String getMessage() {
        return message;
    }

    // Returns validation messages keyed by field name, when validation failed.
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    // Returns the time when this error response was created.
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
