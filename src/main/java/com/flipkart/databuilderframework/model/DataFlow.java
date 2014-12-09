package com.flipkart.databuilderframework.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Flow specification for execution
 */
public class DataFlow implements Serializable {
    /**
     * The name of the flow for execution.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private String name;

    /**
     * A simple description for the flow.
     */
    @JsonProperty
    private String description;

    /**
     * The target data for the flow to generate.
     * The executor system will try to take an instance of the flow to the final state by getting more and more data,
     * and generating more data from that.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private String targetData;

    /**
     * The objects to be used for generating
     * Key is the data for which conflict can arise. Value is the builder to actually use.
     */
    @JsonProperty
    private Map<String, String> resolutionSpecs;

    /**
     * The deduced {@link com.flipkart.databuilderframework.model.ExecutionGraph} for this flow.
     */
    private ExecutionGraph executionGraph;

    /*
     * A set specifying which data elements are transient in this flow. A transient data will not be saved in the data
     * set. It's lifetime is bounded to the request scope.
     */
    private Set<String> transients;

    /**
     * Flag to check if the data flow can be instantiated. On by default.
     */
    private boolean enabled = true;

    /**
     * Flag to not run loops
     */
    private boolean loopingEnabled = true;

    public DataFlow() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetData() {
        return targetData;
    }

    public void setTargetData(String targetData) {
        this.targetData = targetData;
    }

    public ExecutionGraph getExecutionGraph() {
        return executionGraph;
    }

    public void setExecutionGraph(ExecutionGraph executionGraph) {
        this.executionGraph = executionGraph;
    }

    public Map<String, String> getResolutionSpecs() {
        return resolutionSpecs;
    }

    public void setResolutionSpecs(Map<String, String> resolutionSpecs) {
        this.resolutionSpecs = resolutionSpecs;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getTransients() {
        return transients;
    }

    public void setTransients(Set<String> transients) {
        this.transients = transients;
    }

    public boolean isLoopingEnabled() {
        return loopingEnabled;
    }

    public void setLoopingEnabled(boolean loopingEnabled) {
        this.loopingEnabled = loopingEnabled;
    }
}
