package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.model.DataExecutionResponse;

import java.util.Map;

public class RateLimitException extends Exception {
    private Map<String,Object> details;
    private DataExecutionResponse response;
    public enum ErrorCode {
        RATE_LIMITED
    }

    private final ErrorCode errorCode;


    public RateLimitException(String message) {
        super(message);
        this.errorCode = ErrorCode.RATE_LIMITED;
    }

    public RateLimitException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public RateLimitException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public RateLimitException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(message);
        this.details=details;
        this.errorCode = errorCode;
    }

    public RateLimitException(ErrorCode errorCode, String message, Map<String, Object> details, Throwable cause) {
        super(message, cause);
        this.details=details;
        this.errorCode = errorCode;
    }

    public RateLimitException(ErrorCode errorCode, String message, DataExecutionResponse response, Map<String, Object> details, Throwable cause) {
        super(message, cause);
        this.details=details;
        this.errorCode = errorCode;
        this.response = response;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public DataExecutionResponse getResponse() {
        return response;
    }

    public void setResponse(DataExecutionResponse response) {
        this.response = response;
    }
}
