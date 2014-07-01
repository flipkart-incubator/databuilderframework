package com.flipkart.cp.convert.europa.databuilderframework;

import com.flipkart.cp.convert.europa.databuilderframework.engine.*;
import com.flipkart.cp.convert.europa.databuilderframework.engine.impl.DataBuilderFactoryImpl;
import com.flipkart.cp.convert.europa.databuilderframework.model.*;
import com.flipkart.cp.convert.europa.databuilderframework.util.DataSetAccessor;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConditionalFlowTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new DataFlowExecutor(new DataBuilderFactoryImpl(dataBuilderMetadataManager));
    private DataFlowBuilder dataFlowBuilder = new DataFlowBuilder(dataBuilderMetadataManager);
    private DataFlow dataFlow = new DataFlow();
    private DataFlow dataFlowError = new DataFlow();

    public static final class ConditionalBuilder extends DataBuilder {

        @Override
        public Data process(DataBuilderContext context) throws DataBuilderException {
            DataSetAccessor accessor = new DataSetAccessor(context.getDataSet());
            TestDataC dataC = accessor.get("C", TestDataC.class);
            TestDataD dataD = accessor.get("D", TestDataD.class);
            if(dataC.getValue().equals("Hello World")
                    && dataD.getValue().equalsIgnoreCase("this")) {
                return new TestDataE("Wah wah!!");
            }
            return null;
        }
    }

    @Before
    public void setup() throws Exception {
        dataBuilderMetadataManager.register(Lists.newArrayList("A", "B"), "C", "BuilderA", TestBuilderA.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("C", "D"), "E", "BuilderB", ConditionalBuilder.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("C", "E"), "F", "BuilderC", TestBuilderC.class );

        dataFlow.setTargetData("F");
        dataFlow.setExecutionGraph(dataFlowBuilder.generateGraph(dataFlow).deepCopy());
        dataFlowError.setTargetData("Y");
        dataFlowError.setExecutionGraph(dataFlowBuilder.generateGraph(dataFlowError).deepCopy());
    }

    @Test
    public void testRunStop() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataA("Hello")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertTrue(response.getResponses().isEmpty());
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataB("Bhai")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertFalse(response.getResponses().isEmpty());
            Assert.assertEquals("C",response.getResponses().get("BuilderA").getData());
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataD("this")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertTrue(response.getResponses().isEmpty());
        }

    }

    @Test
    public void testRun() throws Exception {
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
            Assert.assertEquals("C",response.getResponses().get("BuilderA").getData());
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataD("this")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertFalse(response.getResponses().isEmpty());
            Assert.assertEquals("E", response.getResponses().get("BuilderB").getData());
            Assert.assertEquals("F",response.getResponses().get("BuilderC").getData());
        }

    }

}
