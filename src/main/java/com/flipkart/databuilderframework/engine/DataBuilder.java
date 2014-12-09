package com.flipkart.databuilderframework.engine;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataBuilderMeta;

/**
 * The core actor that builds data for the system.
 * It uses {@link com.flipkart.databuilderframework.model.Data} stored in the {@link com.flipkart.databuilderframework.model.DataSet}.
 * All classes in the classpath derived from this class and annotated with
 * {@link com.flipkart.databuilderframework.annotations.DataBuilderInfo}
 * will be made available and databuilders in the system.
 */
@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
public abstract class DataBuilder {

    /**
     * The metadata derived from the {@link com.flipkart.databuilderframework.annotations.DataBuilderInfo}
     * annotation on the implementation.
     */
    private DataBuilderMeta dataBuilderMeta;

    /**
     * Data generation function. It will generate the data, modify the DataSet contained in the context and also return
     * freshly generated data.
     * <em>Note:</em> Do not put data members.
     * <em>Note 2:</em> Keep a no-args constructor to be used by the {@link DataBuilderFactory}
     *
     * @param context The context object that contains the {@link com.flipkart.databuilderframework.model.DataSet} available for this operation.
     * @return Returns the {@link com.flipkart.databuilderframework.model.Data} to generated.
     *         It will be added to the active working data-set by the system.
     * @throws java.lang.Exception in case any downstream system or itself errors out.
     * (This is generic, as the upper layers need to be prepared for it).
     */
    abstract public Data process(final DataBuilderContext context) throws DataBuilderException;

    public DataBuilderMeta getDataBuilderMeta() {
        return dataBuilderMeta;
    }

    public void setDataBuilderMeta(DataBuilderMeta dataBuilderMeta) {
        this.dataBuilderMeta = dataBuilderMeta;
    }
}
