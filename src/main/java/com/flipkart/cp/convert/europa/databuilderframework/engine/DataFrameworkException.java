package com.flipkart.cp.convert.europa.databuilderframework.engine;


import java.util.Map;

public class DataFrameworkException extends Exception {
    private Map<String,Object> details;
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public static enum ErrorCode {
        NO_BUILDER_FOR_DATA,
        BUILDER_EXISTS,
        NO_TARGET_DATA,
        NO_BUILDER_FOUND_FOR_NAME,
        INSTANTIATION_FAILURE,
        BUILDER_RESOLUTION_CONFLICT_FOR_DATA,
        BUILDER_EXECUTION_ERROR
    }

    private final ErrorCode errorCode;

    public DataFrameworkException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DataFrameworkException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public DataFrameworkException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details=details;
    }

    public DataFrameworkException(ErrorCode errorCode, String message, Map<String, Object> details, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details=details;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
