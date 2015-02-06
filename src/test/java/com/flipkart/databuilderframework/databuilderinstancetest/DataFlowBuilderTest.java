package com.flipkart.databuilderframework.databuilderinstancetest;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.engine.*;
import com.flipkart.databuilderframework.model.*;
import org.junit.Assert;
import org.junit.Test;

public class DataFlowBuilderTest {

    private static final class TestDataA extends DataAdapter<TestDataA> {
        private final String value;
        public TestDataA(final String value) {
            super(TestDataA.class);
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static final class TestDataB extends DataAdapter<TestDataB> {
        private final String value;
        public TestDataB(final String value) {
            super(TestDataB.class);
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static final class TestDataC extends DataAdapter<TestDataC> {
        private final String value;
        public TestDataC(final String value) {
            super(TestDataC.class);
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @DataBuilderClassInfo(produces = TestDataC.class, consumes = {TestDataA.class, TestDataB.class})
    private static final class BuilderA extends DataBuilder {
        @Override
        public Data process(DataBuilderContext context) throws DataBuilderException {
            DataSetAccessor dataSetAccessor = context.getDataSet().accessor();
            return new TestDataC(dataSetAccessor.get(TestDataA.class).getValue()
                                + " "
                                + dataSetAccessor.get(TestDataB.class).getValue());
        }
    }

    @Test
    public void testInstantiatingBuilder() throws Exception {
        DataFlow dataFlow = new DataFlowBuilder()
                                        .withDataBuilder(new BuilderA())
                                        .withTargetData(TestDataC.class)
                                        .build();
        DataFlowExecutor executor = new SimpleDataFlowExecutor();
        DataFlowInstance instance = new DataFlowInstance();
        instance.setDataFlow(dataFlow);
        DataExecutionResponse response = executor.run(instance,
                                                      new DataDelta(
                                                          new TestDataA("Hello"),
                                                          new TestDataB("Santanu")));
        Assert.assertTrue(response.getResponses().containsKey(TestDataC.class.getCanonicalName()));
    }

}