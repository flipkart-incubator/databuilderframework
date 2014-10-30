package com.flipkart.cp.convert.europa.databuilderframework.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * A graph representation for {@link DataFlow}
 * When a new {@link DataFlow} is created, the system does a data flow
 * analysis and creates a representation of the dependency tree. This is used by the {@link com.flipkart.cp.convert.europa.databuilderframework.engine.DataFlowExecutor}
 * to evaluate and run the data.
 */
public class ExecutionGraph implements Serializable {
    /**
     * A ranked list of builders for each level of dependency tree. The builders at the bottom of the tree come first.
     * Each consecutive level moves towards the root of the tree.
     */
    private List<List<DataBuilderMeta>> dependencyHierarchy;

    /**
     * Instantiates a new Execution graph. For use by deserializers.
     */
    public ExecutionGraph() {
    }

    public ExecutionGraph(List<List<DataBuilderMeta>> dependencyHierarchy) {
        this.dependencyHierarchy = dependencyHierarchy;
    }

    public ExecutionGraph deepCopy() {
        List<List<DataBuilderMeta>> tmpDependencyHierarchy = Lists.newArrayListWithCapacity(dependencyHierarchy.size());
        for(List<DataBuilderMeta> levelMeta : dependencyHierarchy) {
            List<DataBuilderMeta> dataBuilderMetas = Lists.newArrayListWithCapacity(levelMeta.size());
            for(DataBuilderMeta meta : levelMeta) {
                dataBuilderMetas.add(meta.deepCopy());
            }
            tmpDependencyHierarchy.add(dataBuilderMetas);
        }
        return new ExecutionGraph(tmpDependencyHierarchy);
    }

    public List<List<DataBuilderMeta>> getDependencyHierarchy() {
        return dependencyHierarchy;
    }

    public void setDependencyHierarchy(List<List<DataBuilderMeta>> dependencyHierarchy) {
        this.dependencyHierarchy = dependencyHierarchy;
    }
}
