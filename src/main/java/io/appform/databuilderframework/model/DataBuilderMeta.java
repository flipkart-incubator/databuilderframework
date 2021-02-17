package io.appform.databuilderframework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Builder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * Metadata about {@link io.appform.databuilderframework.engine.DataBuilder}.
 * This is used internally by the system to describe the requirements and also the current state of the data in the
 * execution.
 */
@lombok.Data
public class DataBuilderMeta implements Comparable<DataBuilderMeta>, Serializable {
    /**
     * List of {@link io.appform.databuilderframework.model.Data} this {@link io.appform.databuilderframework.engine.DataBuilder}
     * consumes.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private Set<String> consumes;

    /**
     * {@link io.appform.databuilderframework.model.Data} this {@link io.appform.databuilderframework.engine.DataBuilder} generates.
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
     * Set of {@link io.appform.databuilderframework.model.Data} this {@link io.appform.databuilderframework.engine.DataBuilder}
     * can consume optionally, i.e. this {@link io.appform.databuilderframework.model.Data}
     *  presence would trigger {@link io.appform.databuilderframework.engine.DataBuilder} if
     *  all consumes {@link io.appform.databuilderframework.model.Data} are present but its optional and not mandatory for {@link io.appform.databuilderframework.engine.DataBuilder} to run.
     */
    @JsonProperty
    private Set<String> optionals;

    /**
     * Set of {@link io.appform.databuilderframework.model.Data} this {@link io.appform.databuilderframework.engine.DataBuilder}
     * has access to over and above consumes and optionals.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private Set<String> access;

    public DataBuilderMeta(Set<String> consumes, String produces, String name) {
        this(consumes, produces, name, Collections.emptySet(), Collections.emptySet());
    }

    @Builder
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

    public DataBuilderMeta deepCopy() {
    	Set<String> optionalCopy = (optionals != null) ? ImmutableSet.copyOf(optionals) : null;
    	Set<String> accessCopy = (access != null) ? ImmutableSet.copyOf(access) : null;
        return new DataBuilderMeta(ImmutableSet.copyOf(consumes), produces, name, optionalCopy, accessCopy);
    }
}
