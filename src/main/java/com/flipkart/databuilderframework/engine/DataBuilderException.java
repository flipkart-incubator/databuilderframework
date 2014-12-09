package com.flipkart.databuilderframework.engine;


import java.util.Map;

/**
 * Created by ajaysingh on 13/06/14.
 */
public class DataBuilderException extends Exception{
    private Map<String,Object> details;
    public static enum ErrorCode {
        HANDLER_FAILURE
    }

    private final ErrorCode errorCode;


    public DataBuilderException(String message) {
        super(message);
        this.errorCode = ErrorCode.HANDLER_FAILURE;
    }

    public DataBuilderException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DataBuilderException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public DataBuilderException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(message);
        this.details=details;
        this.errorCode = errorCode;
    }

    public DataBuilderException(ErrorCode errorCode, String message, Map<String, Object> details, Throwable cause) {
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
