package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.engine.*;
import com.flipkart.databuilderframework.engine.impl.DataBuilderFactoryImpl;
import com.flipkart.databuilderframework.model.*;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Executors;

import static org.junit.Assert.fail;

public class MultiThreadedDataFlowExecutorTest {
    private static class TestListener implements DataBuilderExecutionListener {

        @Override
        public void beforeExecute(DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) throws Exception {
            System.out.println(builderToBeApplied.getName() + " being called for: " + dataFlowInstance.getId());
        }

        @Override
        public void afterExecute(DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) throws Exception {
            System.out.println(builderToBeApplied.getName() + " called for: " + dataFlowInstance.getId());
        }

        @Override
        public void afterException(DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) throws Exception {
            System.out.println(builderToBeApplied.getName() + " called for: " + dataFlowInstance.getId());
        }
    }
    private static class TestListenerBeforeExecutionError implements DataBuilderExecutionListener {

        @Override
        public void beforeExecute(DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) throws Exception {
            //System.out.println(builderToBeApplied.getName() + " being called for: " + dataFlowInstance.getId());
            throw new Exception("Blah blah");
        }

        @Override
        public void afterExecute(DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) throws Exception {
            System.out.println(builderToBeApplied.getName() + " called for: " + dataFlowInstance.getId());
        }

        @Override
        public void afterException(DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) throws Exception {
            System.out.println(builderToBeApplied.getName() + " called for: " + dataFlowInstance.getId());
        }
    }
    private static class TestListenerAfterExecutionError implements DataBuilderExecutionListener {

        @Override
        public void beforeExecute(DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) throws Exception {
            System.out.println(builderToBeApplied.getName() + " being called for: " + dataFlowInstance.getId());
        }

        @Override
        public void afterExecute(DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) throws Exception {
            //System.out.println(builderToBeApplied.getName() + " called for: " + dataFlowInstance.getId());
            throw new Exception("Blah blah");

        }

        @Override
        public void afterException(DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) throws Exception {
            System.out.println(builderToBeApplied.getName() + " called for: " + dataFlowInstance.getId());
        }
    }

    private static class TestListenerAfterExceptionError implements DataBuilderExecutionListener {

        @Override
        public void beforeExecute(DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) throws Exception {
            System.out.println(builderToBeApplied.getName() + " being called for: " + dataFlowInstance.getId());
        }

        @Override
        public void afterExecute(DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) throws Exception {
            System.out.println(builderToBeApplied.getName() + " called for: " + dataFlowInstance.getId());

        }

        @Override
        public void afterException(DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) throws Exception {
            //System.out.println(builderToBeApplied.getName() + " called for: " + dataFlowInstance.getId());
            throw new Exception("Blah blah");
        }
    }

    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new MultiThreadedDataFlowExecutor(
                                                new DataBuilderFactoryImpl(dataBuilderMetadataManager),
                                                Executors.newFixedThreadPool(2));
    private DataFlowBuilder dataFlowBuilder = new DataFlowBuilder(dataBuilderMetadataManager);
    private DataFlow dataFlow = new DataFlow();
    private DataFlow dataFlowError = new DataFlow();

    @Before
    public void setup() throws Exception {
        dataBuilderMetadataManager.register(Lists.newArrayList("A", "B"), "C", "BuilderA", TestBuilderA.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("C", "D"), "E", "BuilderB", TestBuilderB.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("C", "E"), "F", "BuilderC", TestBuilderC.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("X"), "Y", "BuilderX", TestBuilderError.class );
        //dataBuilderMetadataManager.register(Lists.newArrayList("F"),      "G", "BuilderD", TestBuilderD.class );
        //dataBuilderMetadataManager.register(Lists.newArrayList("E", "C"), "G", "BuilderE", TestBuilderE.class );

        dataFlow.setTargetData("F");
        dataFlow.setExecutionGraph(dataFlowBuilder.generateGraph(dataFlow).deepCopy());
        dataFlowError.setTargetData("Y");
        dataFlowError.setExecutionGraph(dataFlowBuilder.generateGraph(dataFlowError).deepCopy());
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
}
