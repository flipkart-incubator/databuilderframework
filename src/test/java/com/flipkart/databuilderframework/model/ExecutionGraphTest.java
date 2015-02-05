package com.flipkart.databuilderframework.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ExecutionGraphTest {
    @Test
    public void testDeepCopyEmpty() throws Exception {
        ExecutionGraph executionGraph = new ExecutionGraph(Collections.<List<DataBuilderMeta>>emptyList());
        ExecutionGraph executionGraph1 = executionGraph.deepCopy();
        Assert.assertEquals(0, executionGraph1.getDependencyHierarchy().size());
    }

    @Test
    public void testDeepCopy() throws Exception {
        List<DataBuilderMeta> builders = Lists.newArrayList(
                                            new DataBuilderMeta(ImmutableSet.of("A", "B"), "C", "test"));
        ExecutionGraph executionGraph = new ExecutionGraph(Collections.singletonList(builders));
        ExecutionGraph executionGraph1 = executionGraph.deepCopy();
        Assert.assertArrayEquals(executionGraph.getDependencyHierarchy().get(0).toArray(new DataBuilderMeta[executionGraph.getDependencyHierarchy().size()]),
                executionGraph1.getDependencyHierarchy().get(0).toArray(new DataBuilderMeta[executionGraph.getDependencyHierarchy().size()]));
    }
}
