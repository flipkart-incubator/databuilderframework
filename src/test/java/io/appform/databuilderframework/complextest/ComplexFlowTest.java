package io.appform.databuilderframework.complextest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.appform.databuilderframework.engine.DataBuilderMetadataManager;
import io.appform.databuilderframework.engine.DataFlowExecutor;
import io.appform.databuilderframework.engine.ExecutionGraphGenerator;
import io.appform.databuilderframework.engine.SimpleDataFlowExecutor;
import io.appform.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import io.appform.databuilderframework.model.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ComplexFlowTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new SimpleDataFlowExecutor(new InstantiatingDataBuilderFactory(dataBuilderMetadataManager));
    private ExecutionGraphGenerator executionGraphGenerator = new ExecutionGraphGenerator(dataBuilderMetadataManager);
    public ComplexFlowTest() throws Exception {
        dataBuilderMetadataManager.register(ImmutableSet.of("CR", "CAID", "VAS"), "OO", "SB", SB.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("OO"), "POD", "POB", POB.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("OO", "POD", "SPD"), "ILD", "PRB", PRB.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("ILD", "EPD"), "OSD", "PPB", PPB.class);
        dataBuilderMetadataManager.register(ImmutableSet.of("OO", "OSD"), "OCD", "COB", COB.class);
    }

    @Test
    public void testRunWeb() throws Exception {
        DataFlow dataFlow = new DataFlow();

        dataFlow.setName("TestFlow");
        dataFlow.setTargetData("OCD");

        ExecutionGraph e = executionGraphGenerator.generateGraph(dataFlow);
        dataFlow.setExecutionGraph(e);

        DataFlowInstance complexFlowIntsnace = new DataFlowInstance("Test", dataFlow, new DataSet());
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new CR(), new CAID(), new VAS()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            log.info(new ObjectMapper().writeValueAsString(response));
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new SPD()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            log.info(new ObjectMapper().writeValueAsString(response));
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new EPD()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            log.info(new ObjectMapper().writeValueAsString(response));
        }

    }

    @Test
    public void testRunMobile() throws Exception {
        DataFlow dataFlow = new DataFlow();

        dataFlow.setName("TestFlow");
        dataFlow.setTargetData("OCD");

        ExecutionGraph e = executionGraphGenerator.generateGraph(dataFlow);
        dataFlow.setExecutionGraph(e);

        DataFlowInstance complexFlowIntsnace = new DataFlowInstance("Test", dataFlow, new DataSet());
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new CR(), new CAID(), new VAS(), new SPD()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            log.info(new ObjectMapper().writeValueAsString(response));
        }
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new EPD()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            log.info(new ObjectMapper().writeValueAsString(response));
        }

    }

    @Test
    public void testRunSubscription() throws Exception {
        DataFlow dataFlow = new DataFlow();

        dataFlow.setName("TestFlow");
        dataFlow.setTargetData("OCD");

        ExecutionGraph e = executionGraphGenerator.generateGraph(dataFlow);
        dataFlow.setExecutionGraph(e);

        DataFlowInstance complexFlowIntsnace = new DataFlowInstance("Test", dataFlow, new DataSet());
        {
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(new CR(), new CAID(), new VAS(), new SPD(), new EPD()));
            DataExecutionResponse response = executor.run(complexFlowIntsnace, dataDelta);
            log.info(new ObjectMapper().writeValueAsString(response));
        }

    }

}
