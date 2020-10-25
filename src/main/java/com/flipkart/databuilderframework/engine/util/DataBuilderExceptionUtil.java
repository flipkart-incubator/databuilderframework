package com.flipkart.databuilderframework.engine.util;

import com.flipkart.databuilderframework.engine.DataBuilderExecutionListener;
import com.flipkart.databuilderframework.engine.DataBuilderFrameworkException;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;

@UtilityClass
public class DataBuilderExceptionUtil {

    public static void handleExceptionInBeforeExecute(final DataBuilderExecutionListener listener,
                                                      final Logger logger,
                                                      final Throwable t) throws DataBuilderFrameworkException {
        final String errorMessage = "Error running pre-execution execution listener: ";
        logger.error(errorMessage, t);
        if (listener.shouldThrowExceptionInBeforeExecute()) {
            throw new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.BUILDER_PRE_EXECUTION_ERROR,
                    errorMessage + t.getMessage(), t);
        }
    }

    public static void handleExceptionInAfterExecute(final DataBuilderExecutionListener listener,
                                                     final Logger logger,
                                                     final Throwable t) throws DataBuilderFrameworkException {
        final String errorMessage = "Error running post-execution execution listener: ";
        logger.error(errorMessage, t);
        if (listener.shouldThrowExceptionInAfterExecute()) {
            throw new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.BUILDER_POST_EXECUTION_ERROR,
                    errorMessage + t.getMessage(), t);
        }
    }
}
