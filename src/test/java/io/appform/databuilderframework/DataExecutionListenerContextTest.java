package io.appform.databuilderframework;

import io.appform.databuilderframework.engine.*;
import io.appform.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import io.appform.databuilderframework.model.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.fail;

@Slf4j
public class DataExecutionListenerContextTest {
	
	private static final String KEY = "key";
	private static final String VALUE = "value";
    private static class TestListenerWithContextCheck implements DataBuilderExecutionListener {

        @Override
        public void preProcessing(DataFlowInstance dataFlowInstance,
                                  DataDelta dataDelta) throws Exception {
            log.info("Being called for: {}", dataFlowInstance.getId());
        }

        @Override
        public void beforeExecute(DataBuilderContext builderContext,
        						  DataFlowInstance dataFlowInstance,
                                  DataBuilderMeta builderToBeApplied,
                                  DataDelta dataDelta, Map<String, Data> prevResponses) throws Exception {
        	Assert.assertNotNull(builderContext);
        	Assert.assertEquals(builderContext.getContextData(KEY, String.class), VALUE);
        }

        @Override
        public void afterExecute(DataBuilderContext builderContext,
        						 DataFlowInstance dataFlowInstance,
                                 DataBuilderMeta builderToBeApplied,
                                 DataDelta dataDelta, Map<String, Data> prevResponses, Data currentResponse) throws Exception {
        	Assert.assertNotNull(builderContext);
        	Assert.assertEquals(builderContext.getContextData(KEY, String.class), VALUE);
        }

        @Override
        public void afterException(DataBuilderContext builderContext,
        						   DataFlowInstance dataFlowInstance,
                                   DataBuilderMeta builderToBeApplied,
                                   DataDelta dataDelta,
                                   Map<String, Data> prevResponses, Throwable frameworkException) throws Exception {
        	Assert.assertNotNull(builderContext);
        	Assert.assertEquals(builderContext.getContextData(KEY, String.class), VALUE);
        }


        @Override
        public void postProcessing(DataFlowInstance dataFlowInstance,
                                   DataDelta dataDelta, DataExecutionResponse response,
                                   Throwable frameworkException) throws Exception  {
            log.info("Being called for: {}", dataFlowInstance.getId());
        }
    }

    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new SimpleDataFlowExecutor(new InstantiatingDataBuilderFactory(dataBuilderMetadataManager));
    private DataFlow dataFlow = new DataFlow();

    @Before
    public void setup() throws Exception {
        dataFlow = new DataFlowBuilder()
                .withAnnotatedDataBuilder(TestBuilderA.class)
                .withAnnotatedDataBuilder(TestBuilderB.class)
                .withAnnotatedDataBuilder(TestBuilderC.class)
                .withTargetData("F")
                .build();

        executor.registerExecutionListener(new TestListenerWithContextCheck());
    }


    @Test
    public void testBuilderRunWithContextBeingPassed() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId("testflow");
        dataFlowInstance.setDataFlow(dataFlow);
        {
        	DataBuilderContext context = new DataBuilderContext();
        	context.saveContextData(KEY, VALUE);
            DataDelta dataDelta = new DataDelta(Lists.newArrayList(
                                            new TestDataA("Hello"), new TestDataB("World"),
                                            new TestDataD("this"), new TestDataG("Hmmm")));
            DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
            Assert.assertEquals(3, response.getResponses().size());
            Assert.assertTrue(response.getResponses().containsKey("C"));
            Assert.assertTrue(response.getResponses().containsKey("E"));
            Assert.assertTrue(response.getResponses().containsKey("F"));
        }
    }

}
