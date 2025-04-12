package com.flipkart.databuilderframework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * Metadata about {@link com.flipkart.databuilderframework.engine.DataBuilder}.
 * This is used internally by the system to describe the requirements and also the current state of the data in the
 * execution.
 */
@lombok.Data
@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public class DataBuilderMeta implements Comparable<DataBuilderMeta>, Serializable {
    /**
     * List of {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder}
     * consumes.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private final Set<String> consumes;

    /**
     * {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder} generates.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private final String produces;

    /**
     * Name for this builder
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private final String name;

    private final int rank;
    
    
    /**
     * Set of {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder}
     * can consume optionally, i.e. this {@link com.flipkart.databuilderframework.model.Data}
     *  presence would trigger {@link com.flipkart.databuilderframework.engine.DataBuilder} if
     *  all consumes {@link com.flipkart.databuilderframework.model.Data} are present but its optional and not mandatory for {@link com.flipkart.databuilderframework.engine.DataBuilder} to run.
     */
    @JsonProperty
    private final Set<String> optionals;

    /**
     * Set of {@link com.flipkart.databuilderframework.model.Data} this {@link com.flipkart.databuilderframework.engine.DataBuilder}
     * has access to over and above consumes and optionals.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private final Set<String> access;

    private final Set<String> accessibleDataSet;

    private final Set<String> effectiveConsumes;

    public DataBuilderMeta(Set<String> consumes, String produces, String name) {
        this(consumes, produces, name, 0, Collections.emptySet(), Collections.emptySet());
    }

    @Builder
    public DataBuilderMeta(Set<String> consumes, String produces, String name,
                           Set<String> optionals, Set<String> access) {
        this(consumes, produces, name, 0, optionals, access);
    }

    public DataBuilderMeta(Set<String> consumes, String produces, String name,
            int rank,
    		Set<String> optionals, Set<String> access) {
        this.consumes = consumes;
        this.produces = produces;
        this.name = name;
        this.rank = rank;
        this.optionals = optionals;
        this.access = access;
        this.accessibleDataSet = getAllAccessibleDataSet();
        this.effectiveConsumes = getAllEffectiveConsumes();
    }

    @JsonIgnore
    private Set<String> getAllEffectiveConsumes(){
    	if(optionals != null && !optionals.isEmpty()){
    		return Sets.union(optionals, consumes);
    	}else{
    		return consumes;
    	}
    }
    
    @JsonIgnore
    private Set<String> getAllAccessibleDataSet(){
    	Set<String> output = consumes;
    	if(optionals != null && !optionals.isEmpty()) {
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

    public DataBuilderMeta deepCopy() {
    	Set<String> optionalCopy = (optionals != null) ? ImmutableSet.copyOf(optionals) : null;
    	Set<String> accessCopy = (access != null) ? ImmutableSet.copyOf(access) : null;
        return new DataBuilderMeta(ImmutableSet.copyOf(consumes), produces, name, optionalCopy, accessCopy);
    }

    public DataBuilderMeta deepCopy(int rank) {
        Set<String> optionalCopy = (optionals != null) ? ImmutableSet.copyOf(optionals) : null;
        Set<String> accessCopy = (access != null) ? ImmutableSet.copyOf(access) : null;
        return new DataBuilderMeta(ImmutableSet.copyOf(consumes), produces, name, rank, optionalCopy, accessCopy);
    }
}
