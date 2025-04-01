package com.sms.multitenantschool.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    // Constructor for success response
    public ApiResponse(T data, String message) {
        this.success = true;
        this.data = data;
        this.message = message;
        this.error = null;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for error response
    public ApiResponse(String message, String error) {
        this.success = false;
        this.data = null;
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for validation errors
    public ApiResponse(T data, String message, String error) {
        this.success = false;
        this.data = data;
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
