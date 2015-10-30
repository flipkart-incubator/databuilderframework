package com.flipkart.databuilderframework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Nullable;
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
     * Name for this builder
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private String name;

    private int rank;
    
    
    /**
     * Set of {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder}
     * can consume optionally, i.e. this {@link com.flipkart.databuilderframework.model.Data}
     *  presence would trigger {@link com.flipkart.databuilderframework.engine.DataBuilder} if
     *  all consumes {@link com.flipkart.databuilderframework.model.Data} are present but its optional and not mandatory for {@link com.flipkart.databuilderframework.engine.DataBuilder} to run.
     */
    @Nullable
    @JsonProperty
    private Set<String> optionals;

    /**
     * Set of {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder}
     * has access to over and above consumes and optionals.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private Set<String> access;

    public DataBuilderMeta(Set<String> consumes, String produces, String name) {
        this.consumes = consumes;
        this.produces = produces;
        this.name = name;
    }

    //overloaded constructor
    public DataBuilderMeta(Set<String> consumes, String produces, String name, 
    		Set<String> optionals, Set<String> access) {
        this.consumes = consumes;
        this.produces = produces;
        this.name = name;
        this.optionals = optionals;
        this.access = access;
    }

    
    public DataBuilderMeta() {
    }

    public Set<String> getConsumes() {
        return consumes;
    }

    public String getProduces() {
        return produces;
    }

    public String getName() {
        return name;
    }
    
    
    public Set<String> getOptionals() {
        return optionals;
    }
    
    public Set<String> getAccess() {
        return access;
    }

    @JsonIgnore
    public Set<String> getEffectiveConsumes(){
    	if(optionals != null && !optionals.isEmpty()){
    		return Sets.union(optionals, consumes);
    	}else{
    		return consumes;
    	}
    }
    
    @JsonIgnore
    public Set<String> getAccessibleDataSet(){
    	Set<String> output = consumes;
    	if(optionals != null && !optionals.isEmpty()){
    		output = Sets.union(optionals, output);
    	}
    	if(access != null && !access.isEmpty()){
    		output = Sets.union(access, output);
    	}
    	return output;
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
        if (optionals == null && that.optionals != null) return false;
        if (access == null && that.access != null) return false;
        if(optionals != null && !optionals.equals(that.optionals)) return false;
        if(access != null && !access.equals(that.access)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = consumes.hashCode();
        if(optionals != null) result = 31 * result + optionals.hashCode();
        if(access != null) result = 31 * result + access.hashCode();
        result = 31 * result + produces.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public DataBuilderMeta deepCopy() {
    	Set<String> optionalCopy = (optionals != null) ? ImmutableSet.copyOf(optionals) : null;
    	Set<String> accessCopy = (access != null) ? ImmutableSet.copyOf(access) : null;
        return new DataBuilderMeta(ImmutableSet.copyOf(consumes), produces, name, optionalCopy, accessCopy);
    }

    public void setConsumes(Set<String> consumes) {
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
