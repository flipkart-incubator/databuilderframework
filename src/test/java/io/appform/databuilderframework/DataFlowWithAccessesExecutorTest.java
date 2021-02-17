package io.appform.databuilderframework;

import io.appform.databuilderframework.engine.*;
import io.appform.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import io.appform.databuilderframework.model.*;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataFlowWithAccessesExecutorTest {

    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new SimpleDataFlowExecutor(new InstantiatingDataBuilderFactory(dataBuilderMetadataManager));
    private DataFlow dataFlow = new DataFlow();

    @Before
    public void setup() throws Exception {
        dataFlow = new DataFlowBuilder()
                .withAnnotatedDataBuilder(TestBuilderAccesses.class)
                .withTargetData("X")
                .build();
    }

    @Test
    public void withoutAnyAccessData() throws DataBuilderFrameworkException, DataValidationException {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataA("Hello")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            assertFalse(response.getResponses().isEmpty());
            assertTrue(response.getResponses().containsKey("X"));
            if (response.getResponses().get("X") instanceof TestDataX) {
                assertTrue(((TestDataX) response.getResponses().get("X")).getValue().equals("FALSE"));
            } else {
                assertTrue("X not instance of TestDataX", false);
            }
        }
    }

    @Test
    public void withAccessData() throws DataBuilderFrameworkException, DataValidationException {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataA("Hello"), new TestDataD("DD")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            assertFalse(response.getResponses().isEmpty());
            assertTrue(response.getResponses().containsKey("X"));
            if (response.getResponses().get("X") instanceof TestDataX) {
                assertTrue(((TestDataX) response.getResponses().get("X")).getValue().equals("TRUE"));
            } else {
                assertTrue("X not instance of TestDataX", false);
            }
        }
    }
    @Test
    public void withoutOnlyAccessData() throws DataBuilderFrameworkException, DataValidationException {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        {
            DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataD("Hello")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            assertTrue(response.getResponses().isEmpty());
        }
    }


}