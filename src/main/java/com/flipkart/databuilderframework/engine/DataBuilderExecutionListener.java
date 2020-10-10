package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.model.*;

import java.util.Map;

public interface DataBuilderExecutionListener {
    default void preProcessing(DataFlowInstance dataFlowInstance,
                               DataDelta dataDelta) throws Exception {

    }

    void beforeExecute(DataBuilderContext builderContext,
                       DataFlowInstance dataFlowInstance,
                       DataBuilderMeta builderToBeApplied,
                       DataDelta dataDelta,
                       Map<String, Data> prevResponses) throws Exception;

    void afterExecute(DataBuilderContext builderContext,
                      DataFlowInstance dataFlowInstance,
                      DataBuilderMeta builderToBeApplied,
                      DataDelta dataDelta,
                      Map<String, Data> allResponses,
                      Data currentResponse) throws Exception;

    void afterException(
            DataBuilderContext builderContext,
            DataFlowInstance dataFlowInstance,
            DataBuilderMeta builderToBeApplied,
            DataDelta dataDelta,
            Map<String, Data> prevResponses,
            Throwable frameworkException) throws Exception;

    default void postProcessing(DataFlowInstance dataFlowInstance,
                                DataDelta dataDelta,
                                DataExecutionResponse response,
                                Throwable frameworkException) throws Exception {

    }

    default boolean shouldThrowException() {
        return false;
    }

    default boolean shouldThrowExceptionInBeforeExecute() {
        return false;
    }

    default boolean shouldThrowExceptionInAfterExecute() {
        return false;
    }
}
