package com.flipkart.databuilderframework.complextest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.databuilderframework.engine.DataBuilderMetadataManager;
import com.flipkart.databuilderframework.engine.DataFlowBuilder;
import com.flipkart.databuilderframework.engine.DataFlowExecutor;
import com.flipkart.databuilderframework.engine.SimpleDataFlowExecutor;
import com.flipkart.databuilderframework.engine.impl.DataBuilderFactoryImpl;
import com.flipkart.databuilderframework.model.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.junit.Test;

public class ComplexFlowTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new SimpleDataFlowExecutor(new DataBuilderFactoryImpl(dataBuilderMetadataManager));
    private DataFlowBuilder dataFlowBuilder = new DataFlowBuilder(dataBuilderMetadataManager);
    public ComplexFlowTest() throws Exception {
        dataBuilderMetadataManager.register(ImmutableSet.of("CR", "CAID", "VAS"), "OO", "SB", SummaryBuilderTest.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("OO"), "POD", "POB", PaymentOptionBuilderTest.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("OO", "POD", "SPD"), "ILD", "PRB", PaymentRequestBuilderTest.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("ILD", "EPD"), "OSD", "PPB", PaymentProcessorBuilderTest.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("OO", "OSD"), "OCD", "COB", CompleteOrderBuilderTest.class);
    }

    @Test
    public void testRunWeb() throws Exception {
        DataFlow dataFlow = new DataFlow();

        dataFlow.setName("TestFlow");
        dataFlow.setTargetData("OCD");

        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        dataFlow.setExecutionGraph(e);

        DataFlowInstance complexFlowIntsnace = new DataFlowInstance("Test", dataFlow, new DataSet());
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new CartRefTest(), new CartAccountIDTest(), new ValueAddedServiceTest()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            System.out.println(new ObjectMapper().writeValueAsString(response));
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new SelectedPaymentOptionTest()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            System.out.println(new ObjectMapper().writeValueAsString(response));
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new EncryptedPaymentDataTest()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            System.out.println(new ObjectMapper().writeValueAsString(response));
        }

    }

    @Test
    public void testRunMobile() throws Exception {
        DataFlow dataFlow = new DataFlow();

        dataFlow.setName("TestFlow");
        dataFlow.setTargetData("OCD");

        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        dataFlow.setExecutionGraph(e);

        DataFlowInstance complexFlowIntsnace = new DataFlowInstance("Test", dataFlow, new DataSet());
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new CartRefTest(), new CartAccountIDTest(), new ValueAddedServiceTest(), new SelectedPaymentOptionTest()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            System.out.println(new ObjectMapper().writeValueAsString(response));
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new EncryptedPaymentDataTest()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            System.out.println(new ObjectMapper().writeValueAsString(response));
        }

    }

    @Test
    public void testRunSubscription() throws Exception {
        DataFlow dataFlow = new DataFlow();

        dataFlow.setName("TestFlow");
        dataFlow.setTargetData("OCD");

        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        dataFlow.setExecutionGraph(e);

        DataFlowInstance complexFlowIntsnace = new DataFlowInstance("Test", dataFlow, new DataSet());
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new CartRefTest(), new CartAccountIDTest(), new ValueAddedServiceTest(), new SelectedPaymentOptionTest(), new EncryptedPaymentDataTest()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            System.out.println(new ObjectMapper().writeValueAsString(response));
        }

    }

}
