package com.comp4442.expensetracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    // Creates one standard API response object and stamps it with the current time.
    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Builds a successful response that only contains returned data.
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, data);
    }

    // Builds a successful response that contains both a message and returned data.
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // Builds a failed response with an error message and no returned data.
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // Returns whether the API request succeeded.
    public boolean isSuccess() {
        return success;
    }

    // Returns the human-readable response message, when one was provided.
    public String getMessage() {
        return message;
    }

    // Returns the payload data sent back to the client.
    public T getData() {
        return data;
    }

    // Returns the time when this response object was created.
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
