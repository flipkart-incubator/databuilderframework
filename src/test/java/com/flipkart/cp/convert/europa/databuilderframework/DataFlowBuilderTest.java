package com.flipkart.cp.convert.europa.databuilderframework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderMetadataManager;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataFlowBuilder;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataFrameworkException;
import com.flipkart.cp.convert.europa.databuilderframework.model.DataFlow;
import com.flipkart.cp.convert.europa.databuilderframework.model.ExecutionGraph;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.fail;

public class DataFlowBuilderTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlowBuilder dataFlowBuilder = new DataFlowBuilder(dataBuilderMetadataManager);

    @Before
    public void setup() throws Exception {
        dataBuilderMetadataManager.register(Lists.newArrayList("A", "B"), "C", "BuilderA", TestBuilderA.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("C", "D"), "E", "BuilderB", TestBuilderB.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("C", "E"), "F", "BuilderC", TestBuilderC.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("F"),      "G", "BuilderD", TestBuilderD.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("E", "C"), "G", "BuilderE", TestBuilderE.class );
    }

    @Test
    public void testGenerateGraphNoTarget() throws Exception {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        try {
            dataFlowBuilder.generateGraph(dataFlow);
        } catch (DataFrameworkException e) {
            if(DataFrameworkException.ErrorCode.NO_TARGET_DATA == e.getErrorCode()) {
                return;
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected error");
        }
    }

    @Test
    public void testGenerateGraphEmptyTarget() throws Exception {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("");
        try {
            dataFlowBuilder.generateGraph(dataFlow);
        } catch (DataFrameworkException e) {
            if(DataFrameworkException.ErrorCode.NO_TARGET_DATA == e.getErrorCode()) {
                return;
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected error");
        }
    }

    @Test
    public void testGenerateGraphNoExecutors() throws Exception {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("X");
        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        Assert.assertTrue(e.getDependencyHierarchy().isEmpty());
    }

    @Test
    public void testGenerate() throws Exception {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("C");
        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        Assert.assertFalse(e.getDependencyHierarchy().isEmpty());
        Assert.assertEquals(1, e.getDependencyHierarchy().size());
        Assert.assertEquals("BuilderA", e.getDependencyHierarchy().get(0).get(0).getName());
    }

    @Test
    public void testGenerateTwoStep() throws Exception  {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("E");
        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        Assert.assertFalse(e.getDependencyHierarchy().isEmpty());
        Assert.assertEquals(2, e.getDependencyHierarchy().size());
        Assert.assertEquals("BuilderA", e.getDependencyHierarchy().get(0).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(0).size());
        Assert.assertEquals("BuilderB", e.getDependencyHierarchy().get(1).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(1).size());
    }

    @Test
    public void testGenerateInterdependentStep() throws Exception  {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("F");
        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        Assert.assertEquals(3, e.getDependencyHierarchy().size());
        Assert.assertEquals("BuilderA", e.getDependencyHierarchy().get(0).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(0).size());
        Assert.assertEquals("BuilderB", e.getDependencyHierarchy().get(1).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(1).size());
        Assert.assertEquals("BuilderC", e.getDependencyHierarchy().get(2).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(2).size());
    }

    @Test
    public void testGenerateInterdependentStepConflict() throws Exception  {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("G");
        try {
            ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        } catch (DataFrameworkException e) {
            if(DataFrameworkException.ErrorCode.BUILDER_RESOLUTION_CONFLICT_FOR_DATA == e.getErrorCode()) {
                return;
            }
        }
        fail("A conflict should have come here");
    }

    @Test
    public void testGenerateInterdependentStepConflictNoData() throws Exception  {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("G");
        dataFlow.setResolutionSpecs(Collections.singletonMap("G", "aa"));
        try {
            ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        } catch (DataFrameworkException e) {
            if(DataFrameworkException.ErrorCode.NO_BUILDER_FOR_DATA == e.getErrorCode()) {
                return;
            }
        }
        fail("A conflict should have come here");
    }
    @Test
    public void testGenerateInterdependentStepWithResolution() throws Exception  {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("G");
        dataFlow.setResolutionSpecs(Collections.singletonMap("G", "BuilderE"));
        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        Assert.assertEquals(3, e.getDependencyHierarchy().size());
        Assert.assertEquals("BuilderA", e.getDependencyHierarchy().get(0).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(0).size());
        Assert.assertEquals("BuilderB", e.getDependencyHierarchy().get(1).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(1).size());
        Assert.assertEquals("BuilderE", e.getDependencyHierarchy().get(2).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(2).size());

    }
    @Test
    public void testGenerateInterdependentStepWithResolutionAlt() throws Exception  {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("G");
        dataFlow.setResolutionSpecs(Collections.singletonMap("G", "BuilderD"));
        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        System.out.println(new ObjectMapper().writeValueAsString(e));
        Assert.assertEquals(4, e.getDependencyHierarchy().size());
        Assert.assertEquals("BuilderA", e.getDependencyHierarchy().get(0).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(0).size());
        Assert.assertEquals("BuilderB", e.getDependencyHierarchy().get(1).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(1).size());
        Assert.assertEquals("BuilderC", e.getDependencyHierarchy().get(2).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(2).size());
        Assert.assertEquals("BuilderD", e.getDependencyHierarchy().get(3).get(0).getName());

    }

}
