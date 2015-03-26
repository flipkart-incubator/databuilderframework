package com.flipkart.databuilderframework.engine;

import java.util.Map;

/**
 * Created by kaustav.das on 26/03/15.
 */
public class DataValidationException extends Exception {
    private Map<String,Object> details;
    public static enum ErrorCode {
        DATA_VALIDATION_EXCEPTION
    }

    private final ErrorCode errorCode;


    public DataValidationException(String message) {
        super(message);
        this.errorCode = ErrorCode.DATA_VALIDATION_EXCEPTION;
    }

    public DataValidationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DataValidationException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public DataValidationException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(message);
        this.details=details;
        this.errorCode = errorCode;
    }

    public DataValidationException(ErrorCode errorCode, String message, Map<String, Object> details, Throwable cause) {
        super(message, cause);
        this.details=details;
        this.errorCode = errorCode;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
