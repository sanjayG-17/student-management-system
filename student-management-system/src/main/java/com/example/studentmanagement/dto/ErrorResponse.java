package com.example.studentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private boolean success;
    private int status;
    private String error;
    private String message;
    private Map<String, String> errors;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // Default Constructor
    public ErrorResponse() {
        this.success = false;
        this.timestamp = LocalDateTime.now();
    }

    // All-Args Constructor
    public ErrorResponse(boolean success, int status, String error, String message, Map<String, String> errors) {
        this.success = success;
        this.status = status;
        this.error = error;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Map<String, String> getErrors() { return errors; }
    public void setErrors(Map<String, String> errors) { this.errors = errors; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // Manual Builder Pattern
    public static class ErrorResponseBuilder {
        private boolean success = false;
        private int status;
        private String error;
        private String message;
        private Map<String, String> errors;

        public ErrorResponseBuilder success(boolean success) { this.success = success; return this; }
        public ErrorResponseBuilder status(int status) { this.status = status; return this; }
        public ErrorResponseBuilder error(String error) { this.error = error; return this; }
        public ErrorResponseBuilder message(String message) { this.message = message; return this; }
        public ErrorResponseBuilder errors(Map<String, String> errors) { this.errors = errors; return this; }

        public ErrorResponse build() {
            return new ErrorResponse(success, status, error, message, errors);
        }
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }
}
