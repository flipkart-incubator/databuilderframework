package com.flipkart.cp.convert.europa.databuilderframework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Metadata about {@link com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilder}.
 * This is used internally by the system to describe the requirements and also the current state of the data in the
 * execution.
 */
public class DataBuilderMeta implements Comparable<DataBuilderMeta>, Serializable {
    /**
     * List of {@link com.flipkart.cp.convert.europa.databuilderframework.model.Data} this {@link com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilder}
     * consumes.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private List<String> consumes;

    /**
     * {@link com.flipkart.cp.convert.europa.databuilderframework.model.Data} this {@link com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilder} generates.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private String produces;

    /**
     * Name for this builder
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private String name;

    /**
     * Whether this builder has run once or not.
     */
    @JsonIgnore
    private boolean processed = false;

    private int rank;

    public DataBuilderMeta(List<String> consumes, String produces, String name) {
        this.consumes = consumes;
        this.produces = produces;
        this.name = name;
    }

    public DataBuilderMeta(List<String> consumes, String produces, String name, boolean processed) {
        this.consumes = consumes;
        this.produces = produces;
        this.name = name;
        this.processed = processed;
    }

    public DataBuilderMeta() {
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public String getProduces() {
        return produces;
    }

    public String getName() {
        return name;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public int compareTo(DataBuilderMeta rhs) {
        return name.compareTo(rhs.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataBuilderMeta that = (DataBuilderMeta) o;

        if (processed != that.processed) return false;
        if (!consumes.equals(that.consumes)) return false;
        if (!name.equals(that.name)) return false;
        if (!produces.equals(that.produces)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = consumes.hashCode();
        result = 31 * result + produces.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (processed ? 1 : 0);
        return result;
    }

    public DataBuilderMeta deepCopy() {
        return new DataBuilderMeta(new ArrayList<String>(consumes), produces, name, processed);
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public void setProduces(String produces) {
        this.produces = produces;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
