package io.appform.databuilderframework.engine;


import io.appform.databuilderframework.model.DataExecutionResponse;

import java.util.Map;

public class DataBuilderFrameworkException extends Exception {
    private Map<String,Object> details;
	private DataExecutionResponse partialExecutionResponse;
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public static enum ErrorCode {
        PRE_PROCESSING_ERROR,
        POST_PROCESSING_ERROR,
        NO_FACTORY_FOR_DATA_BUILDER,
        NO_BUILDER_FOR_DATA,
        BUILDER_EXISTS,
        NO_TARGET_DATA,
        NO_BUILDER_FOUND_FOR_NAME,
        INSTANTIATION_FAILURE,
        BUILDER_RESOLUTION_CONFLICT_FOR_DATA,
        BUILDER_EXECUTION_ERROR
    }

    private final ErrorCode errorCode;

    public DataBuilderFrameworkException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DataBuilderFrameworkException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public DataBuilderFrameworkException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details=details;
    }

    public DataBuilderFrameworkException(ErrorCode errorCode, String message, Map<String, Object> details, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details=details;
    }

	public DataBuilderFrameworkException(ErrorCode errorCode, String message, Map<String, Object> details, Throwable cause, DataExecutionResponse partialExecutionResponse){
		super(message, cause);
		this.errorCode = errorCode;
		this.details = details;
		this.partialExecutionResponse = partialExecutionResponse;
	}

    public Map<String, Object> getDetails() {
        return details;
    }

	public DataExecutionResponse getPartialExecutionResponse() {
		return partialExecutionResponse;
	}
}
