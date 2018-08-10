package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.engine.*;
import com.flipkart.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import com.flipkart.databuilderframework.model.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class NullableDataTest {

    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowExecutor executor = new SimpleDataFlowExecutor(new InstantiatingDataBuilderFactory(dataBuilderMetadataManager));
    private DataFlow dataFlow;

    public static final class A extends DataAdapter<A> {

        private boolean retry;

        public A() {
            super(A.class);
        }

        public boolean isRetry() {
            return retry;
        }

        public void setRetry(boolean retry) {
            this.retry = retry;
        }
    }

    public static final class B extends DataAdapter<B> {

        public B() {
            super(B.class);
        }

    }

    public static final class RepeatableBuilder extends DataBuilder {

        @Override
        public Data process(DataBuilderContext context) {
            val accessor = context.getDataSet().accessor();
            val retryableData = accessor.get(A.class);
            if (retryableData.isRetry()) {
                return null;
            }
            return new B();
        }

    }


    @Before
    public void setup() throws Exception {
        dataBuilderMetadataManager.register(ImmutableSet.of("A"), "B", "BuilderA", RepeatableBuilder.class);
        dataFlow = new DataFlowBuilder()
                .withMetaDataManager(dataBuilderMetadataManager)
                .withTargetData("B")
                .build();
    }

    @Test
    public void testRunMultipleTimes() throws Exception {
        DataFlowInstance dataFlowInstance = new DataFlowInstance();
        dataFlowInstance.setId(UUID.randomUUID().toString());
        dataFlowInstance.setDataFlow(dataFlow);

        DataDelta dataDelta = new DataDelta(Lists.newArrayList(new A()));
        DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
        Assert.assertEquals(1, response.getResponses().size());

        A a = new A();
        a.setRetry(true);

        dataDelta = new DataDelta(Lists.<Data>newArrayList(a));
        response = executor.run(dataFlowInstance, dataDelta);
        Assert.assertEquals(0, response.getResponses().size());
    }
}
