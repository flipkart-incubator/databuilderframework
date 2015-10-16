package com.flipkart.databuilderframework.speed;

import com.flipkart.databuilderframework.engine.*;
import com.flipkart.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import com.flipkart.databuilderframework.model.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;

public class ConcurrencyWithOptionalsTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new MultiThreadedDataFlowExecutor(
                                                new InstantiatingDataBuilderFactory(dataBuilderMetadataManager),
                                                Executors.newFixedThreadPool(10));
    private DataFlowExecutor simpleExecutor = new SimpleDataFlowExecutor(
                                                new InstantiatingDataBuilderFactory(dataBuilderMetadataManager));
    private ExecutionGraphGenerator executionGraphGenerator = new ExecutionGraphGenerator(dataBuilderMetadataManager);

    @Before
    public void setup() throws Exception {
        dataBuilderMetadataManager.registerWithOptionals(ImmutableSet.of("REQ"),ImmutableSet.of("ADD_REQ"), "A", "BuilderA", ServiceCallerA.class );
        dataBuilderMetadataManager.registerWithOptionals(ImmutableSet.of("REQ"),ImmutableSet.of("ADD_REQ"),  "B", "BuilderB", ServiceCallerB.class );
        dataBuilderMetadataManager.registerWithOptionals(ImmutableSet.of("REQ"),ImmutableSet.of("ADD_REQ"),  "C", "BuilderC", ServiceCallerC.class );
        dataBuilderMetadataManager.registerWithOptionals(ImmutableSet.of("REQ"),ImmutableSet.of("ADD_REQ"),  "D", "BuilderD", ServiceCallerD.class );
        dataBuilderMetadataManager.registerWithOptionals(ImmutableSet.of("ADD_REQ"),ImmutableSet.of("REQ"),  "E", "BuilderE", ServiceCallerE.class );
        dataBuilderMetadataManager.registerWithOptionals(ImmutableSet.of("REQ"),ImmutableSet.of("ADD_REQ"), "F", "BuilderF", ServiceCallerF.class );
        dataBuilderMetadataManager.registerWithOptionals(ImmutableSet.of("ADD_REQ"),ImmutableSet.of("REQ)"), "G", "BuilderG", ServiceCallerG.class );
        dataBuilderMetadataManager.registerWithOptionals(ImmutableSet.of("A", "B", "C", "D"), ImmutableSet.of("E","F","G"), "RES", "ResponseBuilder", DataCombiner.class );

    }

    @Test
    public void testSpeed() throws Exception {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setTargetData("RES");
        ExecutionGraph executionGraph = executionGraphGenerator.generateGraph(dataFlow);
        dataFlow.setExecutionGraph(executionGraph);

        long mTime = 0 ;
        for(int i = 0; i < 1000; i++) {
            DataFlowInstance dataFlowInstance = new DataFlowInstance();
            dataFlowInstance.setId("testflow");
            dataFlowInstance.setDataFlow(dataFlow);
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new RequestData()));
            long startTime = System.currentTimeMillis();
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            mTime += (System.currentTimeMillis() - startTime);
//            System.out.println(response.getResponses().keySet());
//            Assert.assertEquals(5, response.getResponses().size());
            //System.out.println("MT:" + System.currentTimeMillis());
        }
        long sTime = 0;
        for(int i = 0; i < 1000; i++) {
            DataFlowInstance dataFlowInstance = new DataFlowInstance();
            dataFlowInstance.setId("testflow");
            dataFlowInstance.setDataFlow(dataFlow);
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new RequestData()));
            long startTime = System.currentTimeMillis();
            DataExecutionResponse response = simpleExecutor.run(dataFlowInstance, dataDelta);
            sTime += (System.currentTimeMillis() - startTime);
//            System.out.println(response.getResponses().keySet());
//            Assert.assertEquals(5, response.getResponses().size());
            //System.out.println("ST:" + System.currentTimeMillis());
        }
        System.out.println(String.format("MT: %d ST: %d", mTime, sTime));
    }
}
