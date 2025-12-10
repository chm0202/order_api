package com.example.demo.exception;

import java.time.LocalDateTime;

public class ApiErrorResponse {
    private String errorCode;
    private String message;
    private int status;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiErrorResponse(String errorCode, String message, int status) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }

    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}