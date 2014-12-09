package com.flipkart.databuilderframework.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * A instance of the {@link com.flipkart.databuilderframework.model.DataFlow} object to be used for execution.
 * This class represents a running instance of the flow. It contains the flow itself as well as the
 * {@link com.flipkart.databuilderframework.model.DataSet} required for execution.
 */
public class DataFlowInstance {

    /**
     * The ID of the flow instance.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private String id;

    /**
     * The {@link com.flipkart.databuilderframework.model.DataFlow} on which the instance is based.
     */
    @JsonProperty
    private DataFlow dataFlow;

    /**
     * The working {@link com.flipkart.databuilderframework.model.DataSet} for the flow.
     */
    @JsonProperty
    private DataSet dataSet;

    public DataFlowInstance() {
        this.dataSet = new DataSet();
    }

    public DataFlowInstance(String id, DataFlow dataFlow, DataSet dataSet) {
        this.id = id;
        this.dataFlow = dataFlow;
        this.dataSet = dataSet;
    }

    public DataFlow getDataFlow() {
        return dataFlow;
    }

    public void setDataFlow(DataFlow dataFlow) {
        this.dataFlow = dataFlow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }
}
