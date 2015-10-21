package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataBuilderMeta;
import com.flipkart.databuilderframework.model.DataDelta;
import com.flipkart.databuilderframework.model.DataFlowInstance;

import java.util.Map;

public interface DataBuilderExecutionListener {
    public void beforeExecute(DataBuilderContext builderContext,
    						  DataFlowInstance dataFlowInstance,
                              DataBuilderMeta builderToBeApplied,
                              DataDelta dataDelta,
                              Map<String, Data> prevResponses) throws Exception;

    public void afterExecute(DataBuilderContext builderContext,
    						 DataFlowInstance dataFlowInstance,
                             DataBuilderMeta builderToBeApplied,
                             DataDelta dataDelta,
                             Map<String, Data> allResponses,
                             Data currentResponse) throws Exception;

    public void afterException(
    						   DataBuilderContext builderContext,
    						   DataFlowInstance dataFlowInstance,
                               DataBuilderMeta builderToBeApplied,
                               DataDelta dataDelta,
                               Map<String, Data> prevResponses,
                               Throwable frameworkException) throws Exception;
}
