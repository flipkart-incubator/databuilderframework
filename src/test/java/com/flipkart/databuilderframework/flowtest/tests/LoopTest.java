package com.flipkart.databuilderframework.flowtest.tests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.databuilderframework.engine.*;
import com.flipkart.databuilderframework.engine.impl.DataBuilderFactoryImpl;
import com.flipkart.databuilderframework.flowtest.builders.*;
import com.flipkart.databuilderframework.flowtest.data.*;
import com.flipkart.databuilderframework.model.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoopTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new MultiThreadedDataFlowExecutor(new DataBuilderFactoryImpl(dataBuilderMetadataManager), Executors.newFixedThreadPool(10));
    private DataFlowBuilder dataFlowBuilder = new DataFlowBuilder(dataBuilderMetadataManager);
    private ObjectMapper mapper = new ObjectMapper();

    public LoopTest() throws Exception {
        dataBuilderMetadataManager.register(Lists.newArrayList("CR", "CAID"), "OO", "OOB", OmsOrderBuilderTest.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("OO", "OMSP"), "OCRD", "OCRDB", OmsCreateOrderBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("OO", "CAID", "OMSP", "OCRD"), "POD", "PODB", PaymentOptionsBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("SPD", "OO", "OCRD", "POD"), "ILD", "ILDB", IntegrationLogicBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("SPD", "ILD", "OO", "OCRD"), "IPD", "IPDB", InitPaymentBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("IPD", "EPD", "OO"), "DPD", "DPDB", DecryptedPaymentBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("DPD", "IPD", "OO"), "OMSP", "OMSPD", OmsPaymentBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("OMSP", "OO", "DPD"), "PSD", "PSDB", PaymentSummaryBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("PSD", "OMSP", "OO"), "PPD", "PPDB", PaymentPersistedBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("PPD", "PSD"), "OCD", "OCDB", CountingOrderCompletedBuilder.class);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Test
    public void testRunSingleStep() throws Exception {
        DataFlow dataFlow = new DataFlow();

        dataFlow.setName("TestFlow");
        dataFlow.setTargetData("OCD");
        dataFlow.setTransients(Sets.newHashSet("SPD", "EPD", "DPD"));
        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        dataFlow.setExecutionGraph(e);

        DataFlowInstance dataFlowInstance = new DataFlowInstance("Test", dataFlow, new DataSet());
        ExecutionGraph executionGraph = dataFlowInstance.getDataFlow().getExecutionGraph();
        assertNotNull(executionGraph);
        List<List<DataBuilderMeta>> dependencyGraph = executionGraph.getDependencyHierarchy();
        assertEquals(10, dependencyGraph.size());

        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(
                    new CartAccountID(), new CartRef(), new OmsPayments(), new SelectedPaymentOption(), new EncryptedPaymentData()));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            System.out.println(listPrint(response.getResponses().keySet()));
            Assert.assertEquals(9, response.getResponses().size());
        }
        for(List<DataBuilderMeta> metas : dataFlowInstance.getDataFlow().getExecutionGraph().getDependencyHierarchy()) {
            for(DataBuilderMeta meta : metas) {
                meta.setProcessed(false);
            }
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new SelectedPaymentOption(), new EncryptedPaymentData()));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            System.out.println(listPrint(response.getResponses().keySet()));
            Assert.assertEquals(8, response.getResponses().size());
        }
        for(List<DataBuilderMeta> metas : dataFlowInstance.getDataFlow().getExecutionGraph().getDependencyHierarchy()) {
            for(DataBuilderMeta meta : metas) {
                meta.setProcessed(false);
            }
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new SelectedPaymentOption(), new EncryptedPaymentData()));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            System.out.println(listPrint(response.getResponses().keySet()));
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
