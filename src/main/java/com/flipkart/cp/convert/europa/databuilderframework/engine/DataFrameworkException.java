package com.flipkart.cp.convert.europa.databuilderframework.engine;



import com.flipkart.cp.convert.europa.common.execption.BaseException;

import java.util.Map;

public class DataFrameworkException extends BaseException {
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
        super(message, details);
        this.errorCode = errorCode;
    }

    public DataFrameworkException(ErrorCode errorCode, String message, Map<String, Object> details, Throwable cause) {
        super(message, cause, details);
        this.errorCode = errorCode;
    }


}
