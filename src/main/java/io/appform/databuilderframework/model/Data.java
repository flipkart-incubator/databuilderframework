package io.appform.databuilderframework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Data base class. All data classes to be considered by a DataBuilder need to inherit from this
 */
@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "data")
@lombok.Data
@ToString
public abstract class Data {

    /**
     * The type-name for data. This is an app defined tag for this data.
     * Needs to be set by the inheriting class.
     */
    @NotNull
    @JsonIgnore
    private String data;

    /**
     * Indicated which builder generated this data.
     */
    @NotNull
    @JsonIgnore
    private String generatedBy;

    protected Data(String data) {
        this.data = data;
    }

}
