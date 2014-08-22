package com.flipkart.cp.convert.europa.databuilderframework.flowtest.tests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderMetadataManager;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataFlowBuilder;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataFlowExecutor;
import com.flipkart.cp.convert.europa.databuilderframework.engine.SimpleDataFlowExecutor;
import com.flipkart.cp.convert.europa.databuilderframework.engine.impl.DataBuilderFactoryImpl;
import com.flipkart.cp.convert.europa.databuilderframework.flowtest.builders.*;
import com.flipkart.cp.convert.europa.databuilderframework.flowtest.data.*;
import com.flipkart.cp.convert.europa.databuilderframework.model.*;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FlowTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new SimpleDataFlowExecutor(new DataBuilderFactoryImpl(dataBuilderMetadataManager));
    private DataFlowBuilder dataFlowBuilder = new DataFlowBuilder(dataBuilderMetadataManager);
    private ObjectMapper mapper = new ObjectMapper();

    public FlowTest() throws Exception {
        dataBuilderMetadataManager.register(Lists.newArrayList("CR", "CAID"), "OO", "OOB", OmsOrderBuilderTest.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("OO", "OMSP"), "OCRD", "OCRDB", OmsCreateOrderBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("OO", "CAID", "OMSP", "OCRD"), "POD", "PODB", PaymentOptionsBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("SPD", "OO", "OCRD", "POD"), "ILD", "ILDB", IntegrationLogicBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("SPD", "ILD", "OO", "OCRD"), "IPD", "IPDB", InitPaymentBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("IPD", "EPD", "OO"), "DPD", "DPDB", DecryptedPaymentBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("DPD", "IPD", "OO"), "OMSP", "OMSPD", OmsPaymentBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("OMSP", "OO", "DPD"), "PSD", "PSDB", PaymentSummaryBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("PSD", "OMSP", "OO"), "PPD", "PPDB", PaymentPersistedBuilder.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("PPD", "PSD"), "OCD", "OCDB", OrderCompletedBuilder.class);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Test
    public void testRunMultiStep() throws Exception {
        DataFlow dataFlow = new DataFlow();

        dataFlow.setName("TestFlow");
        dataFlow.setTargetData("OCD");

        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        dataFlow.setExecutionGraph(e);

        DataFlowInstance dataFlowInstance = new DataFlowInstance("Test", dataFlow, new DataSet());
        ExecutionGraph executionGraph = dataFlowInstance.getDataFlow().getExecutionGraph();
        assertNotNull(executionGraph);
        List<List<DataBuilderMeta>> dependencyGraph = executionGraph.getDependencyHierarchy();
        assertEquals(10, dependencyGraph.size());

        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(
                    new CartAccountID(), new CartRef(), new OmsPayments()));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            System.out.println(listPrint(response.getResponses().keySet()));
            Assert.assertEquals(3, response.getResponses().size());
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(
                    new SelectedPaymentOption()));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            System.out.println(listPrint(response.getResponses().keySet()));
            Assert.assertEquals(2, response.getResponses().size());
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(
                    new EncryptedPaymentData()));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            System.out.println(listPrint(response.getResponses().keySet()));
            Assert.assertEquals(5, response.getResponses().size());
        }
    }

    @Test
    public void testRunSingleStep() throws Exception {
        DataFlow dataFlow = new DataFlow();

        dataFlow.setName("TestFlow");
        dataFlow.setTargetData("OCD");

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
            Assert.assertEquals(10, response.getResponses().size());
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
