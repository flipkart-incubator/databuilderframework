package com.flipkart.cp.convert.europa.databuilderframework.speed;

import com.flipkart.cp.convert.europa.databuilderframework.*;
import com.flipkart.cp.convert.europa.databuilderframework.engine.*;
import com.flipkart.cp.convert.europa.databuilderframework.engine.impl.DataBuilderFactoryImpl;
import com.flipkart.cp.convert.europa.databuilderframework.model.*;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;

public class ConcurrencyTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new MultiThreadedDataFlowExecutor(
                                                new DataBuilderFactoryImpl(dataBuilderMetadataManager),
                                                Executors.newFixedThreadPool(10));
    private DataFlowExecutor simpleExecutor = new SimpleDataFlowExecutor(
                                                new DataBuilderFactoryImpl(dataBuilderMetadataManager));
    private DataFlowBuilder dataFlowBuilder = new DataFlowBuilder(dataBuilderMetadataManager);

    @Before
    public void setup() throws Exception {
        dataBuilderMetadataManager.register(Lists.newArrayList("REQ"), "A", "BuilderA", ServiceCallerA.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("REQ"), "B", "BuilderB", ServiceCallerB.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("REQ"), "C", "BuilderC", ServiceCallerC.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("REQ"), "D", "BuilderD", ServiceCallerD.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("REQ"), "E", "BuilderE", ServiceCallerE.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("REQ"), "F", "BuilderF", ServiceCallerF.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("REQ"), "G", "BuilderG", ServiceCallerG.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("A", "B", "C", "D", "E", "F", "G"), "RES", "ResponseBuilder", DataCombiner.class );

    }

    @Test
    public void testFunctionality() throws Exception {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setTargetData("RES");
        dataFlow.setExecutionGraph(dataFlowBuilder.generateGraph(dataFlow).deepCopy());
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        long startTime = System.currentTimeMillis();

        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new RequestData()));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertEquals(8, response.getResponses().size());
            Assert.assertTrue(response.getResponses().containsKey("RES"));
        }
        System.out.println(System.currentTimeMillis() - startTime);

    }
    @Test
    public void testSpeed() throws Exception {
        long mTime = 0 ;
        for(int i = 0; i < 1000; i++) {
            DataFlow dataFlow = new DataFlow();
            dataFlow.setTargetData("RES");
            dataFlow.setExecutionGraph(dataFlowBuilder.generateGraph(dataFlow).deepCopy());
            DataFlowInstance dataFlowInstance = new DataFlowInstance();
            dataFlowInstance.setId("testflow");
            dataFlowInstance.setDataFlow(dataFlow);
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new RequestData()));
            long startTime = System.currentTimeMillis();
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            mTime += (System.currentTimeMillis() - startTime);
            Assert.assertEquals(8, response.getResponses().size());
        }
        long sTime = 0;
        for(int i = 0; i < 1000; i++) {
            DataFlow dataFlow = new DataFlow();
            dataFlow.setTargetData("RES");
            dataFlow.setExecutionGraph(dataFlowBuilder.generateGraph(dataFlow).deepCopy());
            DataFlowInstance dataFlowInstance = new DataFlowInstance();
            dataFlowInstance.setId("testflow");
            dataFlowInstance.setDataFlow(dataFlow);
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new RequestData()));
            long startTime = System.currentTimeMillis();
            DataExecutionResponse response = simpleExecutor.run(dataFlowInstance, dataDelta);
            sTime += (System.currentTimeMillis() - startTime);
            Assert.assertEquals(8, response.getResponses().size());
        }
        System.out.println(String.format("MT: %d ST: %d", mTime, sTime));
    }
}
