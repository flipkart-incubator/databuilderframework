package com.flipkart.cp.convert.europa.databuilderframework.engine;

import com.flipkart.cp.convert.europa.databuilderframework.model.Data;
import com.flipkart.cp.convert.europa.databuilderframework.model.DataBuilderMeta;
import com.flipkart.cp.convert.europa.databuilderframework.model.DataDelta;
import com.flipkart.cp.convert.europa.databuilderframework.model.DataFlowInstance;

import java.util.Map;

public interface DataBuilderExecutionListener {
    public void beforeExecute(DataFlowInstance dataFlowInstance,
                              DataBuilderMeta builderToBeApplied,
                              DataDelta dataDelta,
                              Map<String, Data> prevResponses) throws Exception;

    public void afterExecute(DataFlowInstance dataFlowInstance,
                             DataBuilderMeta builderToBeApplied,
                             DataDelta dataDelta,
                             Map<String, Data> allResponses,
                             Data currentResponse) throws Exception;

    public void afterException(DataFlowInstance dataFlowInstance,
                               DataBuilderMeta builderToBeApplied,
                               DataDelta dataDelta,
                               Map<String, Data> prevResponses,
                               Throwable frameworkException) throws Exception;
}
