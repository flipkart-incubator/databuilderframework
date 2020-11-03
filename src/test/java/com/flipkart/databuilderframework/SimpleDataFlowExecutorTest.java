package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderExecutionListener;
import com.flipkart.databuilderframework.engine.DataBuilderFrameworkException;
import com.flipkart.databuilderframework.engine.DataFlowBuilder;
import com.flipkart.databuilderframework.engine.DataFlowExecutor;
import com.flipkart.databuilderframework.engine.SimpleDataFlowExecutor;
import com.flipkart.databuilderframework.engine.impl.MixedDataBuilderFactory;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataBuilderMeta;
import com.flipkart.databuilderframework.model.DataDelta;
import com.flipkart.databuilderframework.model.DataExecutionResponse;
import com.flipkart.databuilderframework.model.DataFlow;
import com.flipkart.databuilderframework.model.DataFlowInstance;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.fail;

@Slf4j
public class SimpleDataFlowExecutorTest {

    private DataFlowExecutor executor = new SimpleDataFlowExecutor(new MixedDataBuilderFactory());
    private DataFlow dataFlow = new DataFlow();

    @Before
    public void setup() throws Exception {
        dataFlow = new DataFlowBuilder()
                .withAnnotatedDataBuilder(TestBuilderA.class)
                .withAnnotatedDataBuilder(TestBuilderB.class)
                .withAnnotatedDataBuilder(TestBuilderC.class)
                .withTargetData("F")
                .build();
    }

    private static class TestListenerBeforeExecutionErrorWithExceptionThrown implements DataBuilderExecutionListener {

        @Override
        public void preProcessing(DataFlowInstance dataFlowInstance,
                                  DataDelta dataDelta) {
            log.info("Being called for: " + dataFlowInstance.getId());
        }

        @Override
        public void beforeExecute(DataBuilderContext builderContext,
                                  DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) throws Exception {
            throw new Exception("Blah blah");
        }

        @Override
        public void afterExecute(DataBuilderContext builderContext,
                                 DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void afterException(DataBuilderContext builderContext,
                                   DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void postProcessing(DataFlowInstance dataFlowInstance,
                                   DataDelta dataDelta, DataExecutionResponse response,
                                   Throwable frameworkException) {
            log.info("Being called for: {}", dataFlowInstance.getId());
        }

        @Override
        public boolean shouldThrowExceptionInBeforeExecute() {
            return true;
        }
    }

    private static class TestListenerAfterExecutionErrorWithExceptionThrown implements DataBuilderExecutionListener {

        @Override
        public void preProcessing(DataFlowInstance dataFlowInstance,
                                  DataDelta dataDelta) {
            log.info("Being called for: {}", dataFlowInstance.getId());
        }


        @Override
        public void beforeExecute(DataBuilderContext builderContext,
                                  DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void afterExecute(DataBuilderContext builderContext,
                                 DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) throws Exception {
            throw new Exception("Blah blah");
        }

        @Override
        public void afterException(DataBuilderContext builderContext,
                                   DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void postProcessing(DataFlowInstance dataFlowInstance,
                                   DataDelta dataDelta, DataExecutionResponse response,
                                   Throwable frameworkException) {
            log.info("Being called for: {}", dataFlowInstance.getId());
        }

        @Override
        public boolean shouldThrowExceptionInAfterExecute() {
            return true;
        }
    }

    @Test
    public void testRunSingleStepWithExceptionThrownInBeforeExecuteInExecutionListener() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);

        executor.registerExecutionListener(new TestListenerBeforeExecutionErrorWithExceptionThrown());

        DataDelta dataDelta = new DataDelta(Lists.newArrayList(
                new TestDataA("Hello"), new TestDataB("World"), new TestDataD("this")));
        try {
            executor.run(dataFlowInstance, dataDelta);
            fail("It should not come here.");
        } catch (DataBuilderFrameworkException exception) {
            Assert.assertEquals(DataBuilderFrameworkException.ErrorCode.BUILDER_PRE_EXECUTION_ERROR, exception.getErrorCode());
        }
    }

    @Test
    public void testRunSingleStepWithExceptionThrownInAfterExecuteInExecutionListener() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);

        executor.registerExecutionListener(new TestListenerAfterExecutionErrorWithExceptionThrown());

        DataDelta dataDelta = new DataDelta(Lists.newArrayList(
                new TestDataA("Hello"), new TestDataB("World"), new TestDataD("this")));
        try {
            executor.run(dataFlowInstance, dataDelta);
            fail("It should not come here.");
        } catch (DataBuilderFrameworkException exception) {
            Assert.assertEquals(DataBuilderFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR, exception.getErrorCode());
        }
    }
}
