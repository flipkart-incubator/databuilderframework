package io.appform.databuilderframework;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.appform.databuilderframework.engine.DataBuilderFrameworkException;
import io.appform.databuilderframework.engine.DataBuilderMetadataManager;
import io.appform.databuilderframework.engine.DataFlowBuilder;
import io.appform.databuilderframework.engine.ExecutionGraphGenerator;
import io.appform.databuilderframework.model.DataFlow;
import io.appform.databuilderframework.model.ExecutionGraph;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.fail;

@Slf4j
public class ExecutionGraphGeneratorTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private ExecutionGraphGenerator executionGraphGenerator = new ExecutionGraphGenerator(dataBuilderMetadataManager);

    @Before
    public void setup() throws Exception {
        dataBuilderMetadataManager.register(ImmutableSet.of("A", "B"), "C", "BuilderA", TestBuilderA.class );
        dataBuilderMetadataManager.register(ImmutableSet.of("C", "D"), "E", "BuilderB", TestBuilderB.class );
        dataBuilderMetadataManager.register(ImmutableSet.of("C", "E"), "F", "BuilderC", TestBuilderC.class );
        dataBuilderMetadataManager.register(ImmutableSet.of("F"),      "G", "BuilderD", TestBuilderD.class );
        dataBuilderMetadataManager.register(ImmutableSet.of("E", "C"), "G", "BuilderE", TestBuilderE.class );
    }

    @Test
    public void testGenerateGraphNoTarget() throws Exception {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        try {
            executionGraphGenerator.generateGraph(dataFlow);
        } catch (DataBuilderFrameworkException e) {
            if(DataBuilderFrameworkException.ErrorCode.NO_TARGET_DATA == e.getErrorCode()) {
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
            executionGraphGenerator.generateGraph(dataFlow);
        } catch (DataBuilderFrameworkException e) {
            if(DataBuilderFrameworkException.ErrorCode.NO_TARGET_DATA == e.getErrorCode()) {
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
        DataFlow dataFlow = new DataFlowBuilder()
                .withMetaDataManager(dataBuilderMetadataManager)
                .withName("test")
                .withTargetData("X")
                .build();
        ExecutionGraph e = dataFlow.getExecutionGraph();
        Assert.assertTrue(e.getDependencyHierarchy().isEmpty());
    }

    @Test
    public void testGenerate() throws Exception {
        DataFlow dataFlow = new DataFlowBuilder()
                .withMetaDataManager(dataBuilderMetadataManager)
                .withName("test")
                .withTargetData("C")
                .build();
        ExecutionGraph e = dataFlow.getExecutionGraph();
        Assert.assertFalse(e.getDependencyHierarchy().isEmpty());
        Assert.assertEquals(1, e.getDependencyHierarchy().size());
        Assert.assertEquals("BuilderA", e.getDependencyHierarchy().get(0).get(0).getName());
    }

    @Test
    public void testGenerateTwoStep() throws Exception  {
        DataFlow dataFlow = new DataFlowBuilder()
                .withMetaDataManager(dataBuilderMetadataManager)
                .withName("test")
                .withTargetData("E")
                .build();
        ExecutionGraph e = dataFlow.getExecutionGraph();
        Assert.assertFalse(e.getDependencyHierarchy().isEmpty());
        Assert.assertEquals(2, e.getDependencyHierarchy().size());
        Assert.assertEquals("BuilderA", e.getDependencyHierarchy().get(0).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(0).size());
        Assert.assertEquals("BuilderB", e.getDependencyHierarchy().get(1).get(0).getName());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(1).size());
    }

    @Test
    public void testGenerateInterdependentStep() throws Exception  {
        DataFlow dataFlow = new DataFlowBuilder()
                .withMetaDataManager(dataBuilderMetadataManager)
                .withName("test")
                .withTargetData("F")
                .build();
        ExecutionGraph e = dataFlow.getExecutionGraph();
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
        try {
            DataFlow dataFlow = new DataFlowBuilder()
                    .withMetaDataManager(dataBuilderMetadataManager)
                    .withName("test")
                    .withTargetData("G")
                    .build();
        } catch (DataBuilderFrameworkException e) {
            if(DataBuilderFrameworkException.ErrorCode.BUILDER_RESOLUTION_CONFLICT_FOR_DATA == e.getErrorCode()) {
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
            ExecutionGraph e = executionGraphGenerator.generateGraph(dataFlow);
        } catch (DataBuilderFrameworkException e) {
            if(DataBuilderFrameworkException.ErrorCode.NO_BUILDER_FOR_DATA == e.getErrorCode()) {
                return;
            }
        }
        fail("A conflict should have come here");
    }
    @Test
    public void testGenerateInterdependentStepWithResolution() throws Exception  {
        final DataFlow dataFlow = new DataFlowBuilder()
                .withMetaDataManager(dataBuilderMetadataManager)
                .withName("test")
                .withTargetData("G")
                .withResolutionSpec("G", "BuilderE")
                .build();
        ExecutionGraph e = dataFlow.getExecutionGraph();
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
        final DataFlow dataFlow = new DataFlowBuilder()
                                    .withMetaDataManager(dataBuilderMetadataManager)
                                    .withName("test")
                                    .withTargetData("G")
                                    .withResolutionSpec("G", "BuilderD")
                                    .build();
        ExecutionGraph e = dataFlow.getExecutionGraph();
        log.info("{}", new ObjectMapper().writeValueAsString(e));
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
