package io.appform.databuilderframework.engine;

import io.appform.databuilderframework.model.*;

import java.util.Map;

public interface DataBuilderExecutionListener {

    default void preProcessing(DataFlowInstance dataFlowInstance,
                               DataDelta dataDelta) throws Exception {

    }

    default void preProcessing(DataBuilderContext dataBuilderContext,
                               DataFlowInstance dataFlowInstance,
                               DataDelta dataDelta) throws Exception {
        preProcessing(dataFlowInstance, dataDelta);
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

    default void postProcessing(DataBuilderContext dataBuilderContext,
                                DataFlowInstance dataFlowInstance,
                                DataDelta dataDelta,
                                DataExecutionResponse response,
                                Throwable frameworkException) throws Exception {
        postProcessing(dataFlowInstance, dataDelta, response, frameworkException);
    }

    default boolean shouldThrowException() {
        return false;
    }
}
