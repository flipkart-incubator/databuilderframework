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
     * List of {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder}
     * optional.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private Set<String> optionals;
    
    
    /**
     * {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder} generates.
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

    private int rank;

    public DataBuilderMeta(Set<String> consumes, Set<String> optionals,
    		String produces, String name) {
        this.consumes = consumes;
        this.optionals = optionals;
        this.produces = produces;
        this.name = name;
    }

    public DataBuilderMeta() {
    }


    public Set<String> getConsumes() {
        return consumes;
    }

    public Set<String> getOptionals() {
        return optionals;
    }
    
    public Set<String> getEffectiveConsumes(){
    	if(null == optionals && optionals.isEmpty()){
    		return consumes;
    	}else{
    		return Sets.union(consumes, optionals);
    	}
    }

    public String getProduces() {
        return produces;
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

        if (!consumes.equals(that.consumes)) return false;
        if (!name.equals(that.name)) return false;
        if (!produces.equals(that.produces)) return false;
        if(!optionals.equals(that.optionals)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = consumes.hashCode();
        result = 31 * result + produces.hashCode();
        result = 31 * result + optionals.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public DataBuilderMeta deepCopy() {
        return new DataBuilderMeta(ImmutableSet.copyOf(consumes),
        		ImmutableSet.copyOf(optionals),produces, name);
    }

    
    public void setConsumes(Set<String> consumes) {
        this.consumes = consumes;
    }
    
    public void setOptionals(Set<String> optionals) {
        this.optionals = optionals;
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
