package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.engine.*;
import com.flipkart.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import com.flipkart.databuilderframework.model.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Executors;

import static org.junit.Assert.fail;

@Slf4j
public class MultiThreadedDataFlowExecutorTest {
    private static class TestListener implements DataBuilderExecutionListener {

        @Override
        public void beforeExecute(DataBuilderContext builderContext,
        						  DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) throws Exception {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void afterExecute(DataBuilderContext builderContext,
        						 DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) throws Exception {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void afterException(DataBuilderContext builderContext,
        						   DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) throws Exception {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

    }
    private static class TestListenerBeforeExecutionError implements DataBuilderExecutionListener {

        @Override
        public void preProcessing(DataFlowInstance dataFlowInstance,
                                  DataDelta dataDelta) throws Exception {
            System.out.println("Being called for: " + dataFlowInstance.getId());
        }

        @Override
        public void beforeExecute(DataBuilderContext builderContext,
        						  DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) throws Exception {
            //System.out.println(builderToBeApplied.getName() + " being called for: " + dataFlowInstance.getId());
            throw new Exception("Blah blah");
        }

        @Override
        public void afterExecute(DataBuilderContext builderContext,
        						 DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) throws Exception {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void afterException(DataBuilderContext builderContext,
        						   DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) throws Exception {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void postProcessing(DataFlowInstance dataFlowInstance,
                                   DataDelta dataDelta, DataExecutionResponse response,
                                   Throwable frameworkException) throws Exception  {
            log.info("Being called for: {}", dataFlowInstance.getId());
        }
    }
    private static class TestListenerAfterExecutionError implements DataBuilderExecutionListener {

        @Override
        public void preProcessing(DataFlowInstance dataFlowInstance,
                                  DataDelta dataDelta) throws Exception {
            log.info("Being called for: {}", dataFlowInstance.getId());
        }


        @Override
        public void beforeExecute(DataBuilderContext builderContext,
        						  DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) throws Exception {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void afterExecute(DataBuilderContext builderContext,
        						 DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) throws Exception {
            //System.out.println(builderToBeApplied.getName() + " called for: " + dataFlowInstance.getId());
            throw new Exception("Blah blah");

        }

        @Override
        public void afterException(DataBuilderContext builderContext,
        						   DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) throws Exception {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void postProcessing(DataFlowInstance dataFlowInstance,
                                   DataDelta dataDelta, DataExecutionResponse response,
                                   Throwable frameworkException) throws Exception  {
            log.info("Being called for: {}", dataFlowInstance.getId());
        }
    }

    private static class TestListenerAfterExceptionError implements DataBuilderExecutionListener {

        @Override
        public void preProcessing(DataFlowInstance dataFlowInstance,
                                  DataDelta dataDelta) throws Exception {
            System.out.println("Being called for: " + dataFlowInstance.getId());
        }


        @Override
        public void beforeExecute(DataBuilderContext builderContext,
        						  DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) throws Exception {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());
        }

        @Override
        public void afterExecute(DataBuilderContext builderContext,
        						 DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) throws Exception {
            log.info("{} called for: {}", builderToBeApplied.getName(), dataFlowInstance.getId());

        }

        @Override
        public void afterException(DataBuilderContext builderContext,
        						   DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) throws Exception {
            //System.out.println(builderToBeApplied.getName() + " called for: " + dataFlowInstance.getId());
            throw new Exception("Blah blah");
        }

        @Override
        public void postProcessing(DataFlowInstance dataFlowInstance,
                                   DataDelta dataDelta, DataExecutionResponse response,
                                   Throwable frameworkException) throws Exception  {
            log.info("Being called for: {}", dataFlowInstance.getId());
        }
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

    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new MultiThreadedDataFlowExecutor(
                                                new InstantiatingDataBuilderFactory(dataBuilderMetadataManager),
                                                Executors.newFixedThreadPool(2));
    private ExecutionGraphGenerator executionGraphGenerator = new ExecutionGraphGenerator(dataBuilderMetadataManager);
    private DataFlow dataFlow = new DataFlow();
    private DataFlow dataFlowError = new DataFlow();
    private DataFlow dataFlowValidationError = new DataFlow();
    private DataFlow dataFlowValidationErrorWithPartialData = new DataFlow();

    @Before
    public void setup() throws Exception {
        dataFlow = new DataFlowBuilder()
                .withAnnotatedDataBuilder(TestBuilderA.class)
                .withAnnotatedDataBuilder(TestBuilderB.class)
                .withAnnotatedDataBuilder(TestBuilderC.class)
                .withTargetData("F")
                .build();

        dataFlowError = new DataFlowBuilder()
                .withAnnotatedDataBuilder(TestBuilderError.class)
                .withTargetData("Y")
                .build();

        dataFlowValidationError = new DataFlowBuilder()
                .withAnnotatedDataBuilder(TestBuilderDataValidationError.class)
                .withTargetData("Y")
                .build();

        dataFlowValidationErrorWithPartialData = new DataFlowBuilder()
                .withAnnotatedDataBuilder(TestBuilderA.class)
                .withAnnotatedDataBuilder(TestBuilderDataValidationError.class)
                .withTargetData("Y")
                .build();

        executor.registerExecutionListener(new TestListener());
        executor.registerExecutionListener(new TestListenerBeforeExecutionError());
        executor.registerExecutionListener(new TestListenerAfterExecutionError());
        executor.registerExecutionListener(new TestListenerAfterExceptionError());
    }

    @Test
    public void testRunThreeSteps() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataA("Hello")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertTrue(response.getResponses().isEmpty());
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataB("World")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertFalse(response.getResponses().isEmpty());
            Assert.assertTrue(response.getResponses().containsKey("C"));
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataD("this")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertFalse(response.getResponses().isEmpty());
            Assert.assertTrue(response.getResponses().containsKey("E"));
            Assert.assertTrue(response.getResponses().containsKey("F"));
        }
    }

    @Test
    public void testRunTwoSteps() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new TestDataA("Hello"), new TestDataB("World")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertFalse(response.getResponses().isEmpty());
            Assert.assertTrue(response.getResponses().containsKey("C"));
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataD("this")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertFalse(response.getResponses().isEmpty());
            Assert.assertTrue(response.getResponses().containsKey("E"));
            Assert.assertTrue(response.getResponses().containsKey("F"));
        }
    }
    @Test
    public void testRunSingleStep() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(
                                            new TestDataA("Hello"), new TestDataB("World"),
                                            new TestDataD("this"), new TestDataG("Hmmm")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertEquals(3, response.getResponses().size());
            Assert.assertTrue(response.getResponses().containsKey("C"));
            Assert.assertTrue(response.getResponses().containsKey("E"));
            Assert.assertTrue(response.getResponses().containsKey("F"));
        }
    }

    @Test
    public void testRunError() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlowError);
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataX("Hello")));
            try {
                executor.run(dataFlowInstance, dataDelta);
            } catch (Exception e) {
                Assert.assertEquals("TestError", e.getCause().getMessage());
                return;
            }
            fail("Should have thrown exception");
        }
    }

    @Test
    public void testRunErrorNPE() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlowError);
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataX("Hello"), null));
            try {
                executor.run(dataFlowInstance, dataDelta);
            } catch (Exception e) {
                return;
            }
            fail("Should have thrown exception");
        }
    }

    @Test
    public void testRunValidationError() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlowValidationError);
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataC("Hello")));
            try {
                executor.run(dataFlowInstance, dataDelta);
            } catch (Exception e) {
                Assert.assertEquals("DataValidationError", e.getCause().getMessage());
                return;
            }
            fail("Should have thrown exception");
        }
    }

    @Test
    public void testRunValidationErrorWithPartialData() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        DataExecutionResponse response = new DataExecutionResponse();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlowValidationErrorWithPartialData);
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataA("Hello"), new TestDataB("World")));
            try {
                response = executor.run(dataFlowInstance, dataDelta);
            } catch (DataValidationException e) {
                DataExecutionResponse dataExecutionResponse = e.getResponse();
                Assert.assertTrue(dataExecutionResponse.getResponses().containsKey("C"));
                return;
            }
            fail("Should have thrown exception");
        }
    }

    @Test
    public void testRunSingleStepWithExceptionThrownInBeforeExecuteInExecutionListener() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        executor.registerExecutionListener(new TestListenerBeforeExecutionErrorWithExceptionThrown());

        DataDelta dataDelta = new DataDelta(Lists.newArrayList(
                new TestDataA("Hello"), new TestDataB("World"),
                new TestDataD("this"), new TestDataG("Hmmm")));
        try {
            executor.run(dataFlowInstance, dataDelta);
            fail("It should not come here.");
        } catch (DataBuilderFrameworkException exception) {
            Assert.assertEquals(DataBuilderFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR, exception.getErrorCode());
        }
    }

    @Test
    public void testRunSingleStepWithExceptionThrownInAfterExecuteInExecutionListener() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        executor.registerExecutionListener(new TestListenerAfterExecutionErrorWithExceptionThrown());

        DataDelta dataDelta = new DataDelta(Lists.newArrayList(
                new TestDataA("Hello"), new TestDataB("World"),
                new TestDataD("this"), new TestDataG("Hmmm")));
        try {
            executor.run(dataFlowInstance, dataDelta);
            fail("It should not come here.");
        } catch (DataBuilderFrameworkException exception) {
            Assert.assertEquals(DataBuilderFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR, exception.getErrorCode());
        }
    }
}
