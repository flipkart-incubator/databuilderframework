package com.flipkart.databuilderframework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.databuilderframework.engine.DataBuilderMetadataManager;
import com.flipkart.databuilderframework.engine.ExecutionGraphGenerator;
import com.flipkart.databuilderframework.engine.DataFlowExecutor;
import com.flipkart.databuilderframework.engine.SimpleDataFlowExecutor;
import com.flipkart.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import com.flipkart.databuilderframework.model.*;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DataFlowWithTransientDataTest {

    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new SimpleDataFlowExecutor(new InstantiatingDataBuilderFactory(dataBuilderMetadataManager));
    private ExecutionGraphGenerator executionGraphGenerator = new ExecutionGraphGenerator(dataBuilderMetadataManager);
    private DataFlow dataFlow = new DataFlow();
    private DataFlow dataFlowError = new DataFlow();

    @Before
    public void setup() throws Exception {
        dataBuilderMetadataManager
                .register(ImmutableSet.of("A", "B"), "C", "BuilderA", TestBuilderA.class )
                .register(ImmutableSet.of("C", "D"), "E", "BuilderB", TestBuilderB.class )
                .register(ImmutableSet.of("A", "E"), "F", "BuilderC", TestBuilderC.class )
                .register(ImmutableSet.of("X"), "Y", "BuilderX", TestBuilderError.class);
        //dataBuilderMetadataManager.register(ImmutableSet.of("F"),      "G", "BuilderD", TestBuilderD.class );
        //dataBuilderMetadataManager.register(ImmutableSet.of("E", "C"), "G", "BuilderE", TestBuilderE.class );

        dataFlow.setTargetData("F");
        dataFlow.setExecutionGraph(executionGraphGenerator.generateGraph(dataFlow).deepCopy());
        dataFlow.setTransients(Sets.newHashSet("C"));
        System.out.println(new ObjectMapper().writeValueAsString(dataFlow));
    }

    @Test
    public void testTransient() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataA("Hello")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertTrue(response.getResponses().isEmpty());
        }
        Data dataC = null;
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataB("World")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertFalse(response.getResponses().isEmpty());
            Assert.assertTrue(response.getResponses().containsKey("C"));
            DataSetAccessor accessor = new DataSetAccessor(dataFlowInstance.getDataSet());
            Assert.assertTrue(accessor.checkForData(ImmutableSet.of("A", "B")));
            Assert.assertFalse(accessor.checkForData("C"));
            dataC = response.getResponses().get("C");
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new TestDataD("this"), dataC));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertFalse(response.getResponses().isEmpty());
            Assert.assertTrue(response.getResponses().containsKey("E"));
            Assert.assertTrue(response.getResponses().containsKey("F"));
        }
    }
}
