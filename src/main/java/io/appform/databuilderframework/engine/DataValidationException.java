package io.appform.databuilderframework.engine;

import io.appform.databuilderframework.model.DataExecutionResponse;

import java.util.Map;

/**
 * Created by kaustav.das on 26/03/15.
 */
public class DataValidationException extends Exception {
    private Map<String,Object> details;
    private DataExecutionResponse response;
    public enum ErrorCode {
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

    public DataValidationException(ErrorCode errorCode, String message, DataExecutionResponse response, Map<String, Object> details, Throwable cause) {
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
