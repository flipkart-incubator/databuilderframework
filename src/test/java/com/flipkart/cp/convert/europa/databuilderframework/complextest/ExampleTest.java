package com.flipkart.cp.convert.europa.databuilderframework.complextest;

import com.flipkart.cp.convert.europa.databuilderframework.TestBuilderA;
import com.flipkart.cp.convert.europa.databuilderframework.TestBuilderB;
import com.flipkart.cp.convert.europa.databuilderframework.TestBuilderC;
import com.flipkart.cp.convert.europa.databuilderframework.TestBuilderD;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderMetadataManager;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataFlowBuilder;
import com.flipkart.cp.convert.europa.databuilderframework.model.DataFlow;
import com.flipkart.cp.convert.europa.databuilderframework.model.ExecutionGraph;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

public class ExampleTest {

    @Test
    public void testBalancedTree() throws Exception {
        DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
        DataFlowBuilder dataFlowBuilder = new DataFlowBuilder(dataBuilderMetadataManager);
        dataBuilderMetadataManager.register(Lists.newArrayList("A", "B"), "C", "BuilderA", TestBuilderA.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("D", "E"), "F", "BuilderC", TestBuilderB.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("C", "F"), "G", "BuilderD", TestBuilderC.class );
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("G");
        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        Assert.assertEquals(2, e.getDependencyHierarchy().size());
        Assert.assertEquals(2, e.getDependencyHierarchy().get(0).size());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(1).size());
    }

    @Test
    public void testDiamond() throws Exception {
        DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
        DataFlowBuilder dataFlowBuilder = new DataFlowBuilder(dataBuilderMetadataManager);
        dataBuilderMetadataManager.register(Lists.newArrayList("A", "B"), "C", "BuilderA", TestBuilderA.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("C", "E"), "F", "BuilderB", TestBuilderB.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("C", "G"), "H", "BuilderC", TestBuilderC.class );
        dataBuilderMetadataManager.register(Lists.newArrayList("F", "H"), "X", "BuilderD", TestBuilderD.class );
        DataFlow dataFlow = new DataFlow();
        dataFlow.setName("test");
        dataFlow.setTargetData("X");
        ExecutionGraph e = dataFlowBuilder.generateGraph(dataFlow);
        Assert.assertEquals(3, e.getDependencyHierarchy().size());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(0).size());
        Assert.assertEquals(2, e.getDependencyHierarchy().get(1).size());
        Assert.assertEquals(1, e.getDependencyHierarchy().get(2).size());
    }

}
