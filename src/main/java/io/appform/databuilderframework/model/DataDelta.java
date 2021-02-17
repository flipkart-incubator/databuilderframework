package io.appform.databuilderframework.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
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

    public DataDelta(Data... data) {
        delta = Arrays.asList(data);
    }
    public DataDelta() {
        delta = Lists.newArrayList();
    }

    public DataDelta(List<Data> delta) {
        this.delta = delta;
    }

    public DataDelta add(Data data) {
        if(null == this.delta) {
            this.delta = Lists.newArrayList();
        }
        this.delta.add(data);
        return this;
    }

    public DataDelta add(List<Data> data) {
        if(null == this.delta) {
            this.delta = Lists.newArrayList(data);
        }
        this.delta.addAll(data);
        return this;
    }

    public List<Data> getDelta() {
        return delta;
    }

    public void setDelta(List<Data> delta) {
        this.delta = delta;
    }
}
