package com.flipkart.cp.convert.europa.databuilderframework;

import com.flipkart.cp.convert.europa.databuilderframework.engine.*;
import com.flipkart.cp.convert.europa.databuilderframework.engine.impl.DataBuilderFactoryImpl;
import com.flipkart.cp.convert.europa.databuilderframework.model.Data;
import com.flipkart.cp.convert.europa.databuilderframework.model.ExecutionGraph;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class DataBuilderFactoryImplTest {
    public static class WrongBuilder extends DataBuilder {

        public WrongBuilder(String blah) {

        }

        @Override
        public Data process(DataBuilderContext context) {
            return null;
        }
    }
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowBuilder dataFlowBuilder = new DataFlowBuilder(dataBuilderMetadataManager);
    private DataBuilderFactory dataBuilderFactory = new DataBuilderFactoryImpl(dataBuilderMetadataManager);

    @Before
    public void setup() throws Exception {
        dataBuilderMetadataManager.register(Lists.newArrayList("A", "B"), "C", "BuilderA", TestBuilderA.class);
        dataBuilderMetadataManager.register(Lists.newArrayList("A", "B"), "C", "BuilderB", null);
        dataBuilderMetadataManager.register(Lists.newArrayList("A", "B"), "X", "BuilderC", WrongBuilder.class);
    }


    @Test
    public void testCreate() throws Exception {
        try {
            Assert.assertNotNull(dataBuilderFactory.create("BuilderA"));
            dataBuilderFactory.create("BuilderB"); //Should throw
        } catch (DataFrameworkException e) {
            if(DataFrameworkException.ErrorCode.NO_BUILDER_FOUND_FOR_NAME == e.getErrorCode()) {
                return;
            }
        }
        fail();
     }

    @Test
    public void testFail() throws Exception {
        ExecutionGraph executionGraph = new ExecutionGraph();
        try {
            dataBuilderFactory.create("BuilderC"); //Should throw
        } catch (DataFrameworkException e) {
            if(DataFrameworkException.ErrorCode.INSTANTIATION_FAILURE == e.getErrorCode()) {
                return;
            }
        }
        fail();
    }
}
