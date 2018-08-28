package com.flipkart.databuilderframework.flowtest.tests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.databuilderframework.engine.DataBuilderMetadataManager;
import com.flipkart.databuilderframework.engine.DataFlowExecutor;
import com.flipkart.databuilderframework.engine.ExecutionGraphGenerator;
import com.flipkart.databuilderframework.engine.MultiThreadedDataFlowExecutor;
import com.flipkart.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import com.flipkart.databuilderframework.flowtest.builders.*;
import com.flipkart.databuilderframework.flowtest.data.*;
import com.flipkart.databuilderframework.model.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class LoopTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new MultiThreadedDataFlowExecutor(new InstantiatingDataBuilderFactory(dataBuilderMetadataManager), Executors.newFixedThreadPool(10));
    private ExecutionGraphGenerator executionGraphGenerator = new ExecutionGraphGenerator(dataBuilderMetadataManager);
    private ObjectMapper mapper = new ObjectMapper();

    public LoopTest() throws Exception {
        dataBuilderMetadataManager.register(ImmutableSet.of("CR", "CAID"), "OO", "OOB", OOBuilderTest.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("OO", "OMSP"), "OCRD", "OCRDB", OCOBuilder.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("OO", "CAID", "OMSP", "OCRD"), "POD", "PODB", POBuilder.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("SPD", "OO", "OCRD", "POD"), "ILD", "ILDB", ILBuilder.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("SPD", "ILD", "OO", "OCRD"), "IPD", "IPDB", IPBuilder.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("IPD", "EPD", "OO"), "DPD", "DPDB", DPBuilder.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("DPD", "IPD", "OO"), "OMSP", "OMSPD", OPBuilder.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("OMSP", "OO", "DPD"), "PSD", "PSDB", PSBuilder.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("PSD", "OMSP", "OO"), "PPD", "PPDB", PPBuilder.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("PPD", "PSD"), "OCD", "OCDB", CountingOCBuilder.class);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Test
    public void testRunSingleStep() throws Exception {
        DataFlow dataFlow = new DataFlow();

        dataFlow.setName("TestFlow");
        dataFlow.setTargetData("OCD");
        dataFlow.setTransients(Sets.newHashSet("SPD", "EPD", "DPD"));
        ExecutionGraph e = executionGraphGenerator.generateGraph(dataFlow);
        dataFlow.setExecutionGraph(e);

        DataFlowInstance dataFlowInstance = new DataFlowInstance("Test", dataFlow, new DataSet());
        ExecutionGraph executionGraph = dataFlowInstance.getDataFlow().getExecutionGraph();
        assertNotNull(executionGraph);
        List<List<DataBuilderMeta>> dependencyGraph = executionGraph.getDependencyHierarchy();
        assertEquals(10, dependencyGraph.size());

        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(
                    new CAID(), new CR(), new OP(), new SPO(), new EPD()));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            log.info(listPrint(response.getResponses().keySet()));
            Assert.assertEquals(9, response.getResponses().size());
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new SPO(), new EPD()));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            log.info(listPrint(response.getResponses().keySet()));
            Assert.assertEquals(8, response.getResponses().size());
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new SPO(), new EPD()));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            log.info(listPrint(response.getResponses().keySet()));
            Assert.assertEquals(7, response.getResponses().size());
        }
    }

    private String listPrint(Collection<String> dataset) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String data : dataset) {
            stringBuilder.append(data + ", ");
        }
        return stringBuilder.toString();
    }

}
