package com.flipkart.databuilderframework.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Data delta to be processed for every execute request.
 * A data-delta will contain all the data being provided to the system. What builders to be run will depend on what
 * delta is being fed to the system.
 */
public class DataDelta {
    /**
     * The list of data being provided for consideration.
     */
    @NotNull
    @NotEmpty
    @JsonProperty
    private List<Data> delta;

    public DataDelta() {
    }

    public DataDelta(List<Data> delta) {
        this.delta = delta;
    }

    public List<Data> getDelta() {
        return delta;
    }

    public void setDelta(List<Data> delta) {
        this.delta = delta;
    }
}
