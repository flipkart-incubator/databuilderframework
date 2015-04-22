package com.flipkart.databuilderframework.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * Metadata about {@link com.flipkart.databuilderframework.engine.DataBuilder}.
 * This is used internally by the system to describe the requirements and also the current state of the data in the
 * execution.
 */
public class DataBuilderMeta implements Comparable<DataBuilderMeta>, Serializable {
    /**
     * List of {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder}
     * consumes.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private Set<String> consumes;

    /**
     * {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder} generates.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private String produces;

    /**
     * Set of {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder} accesses.
     * These are optional for the running of the builder itself.
     * Any change in these <b>will not trigger</b> execution of the builder.
     */
    @NotNull
    @JsonProperty
    private Set<String> accesses;

    /**
     * Name for this builder
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private String name;

    private int rank;

    public DataBuilderMeta(Set<String> consumes, String produces, String name) {
        this.consumes = consumes;
        this.produces = produces;
        this.name = name;
        this.accesses = Sets.newHashSet();
    }

    public DataBuilderMeta(Set<String> consumes, Set<String> accesses, String produces, String name) {
        this.consumes = consumes;
        this.produces = produces;
        this.name = name;
        this.accesses = accesses;
    }

    public DataBuilderMeta() {
    }

    public Set<String> getConsumes() {
        return consumes;
    }

    public String getProduces() {
        return produces;
    }

    public Set<String> getAccesses() {
        return accesses;
    }

    public String getName() {
        return name;
    }

    public int compareTo(DataBuilderMeta rhs) {
        return name.compareTo(rhs.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataBuilderMeta that = (DataBuilderMeta) o;

        return consumes.equals(that.consumes)
                && name.equals(that.name)
                && produces.equals(that.produces)
                && accesses.equals(that.accesses);

    }

    @Override
    public int hashCode() {
        int result = consumes.hashCode();
        result = 31 * result + produces.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + accesses.hashCode();
        return result;
    }

    public DataBuilderMeta deepCopy() {
        return new DataBuilderMeta(ImmutableSet.copyOf(consumes), accesses, produces, name);
    }

    public void setConsumes(Set<String> consumes) {
        this.consumes = consumes;
    }

    public void setAccesses(Set<String> accesses) {
        this.accesses = accesses;
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
