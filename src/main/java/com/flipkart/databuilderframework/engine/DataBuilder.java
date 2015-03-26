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
     * The metadata derived from the {@link com.flipkart.databuilderframework.annotations.DataBuilderInfo} or {@link com.flipkart.databuilderframework.annotations.DataBuilderClassInfo}
     * annotation on the implementation.
     */
    private DataBuilderMeta dataBuilderMeta;

    /**
     * Data generation function. It will generate the data, using the Data present in the DataSet contained in the
     * context and also return freshly generated data.<br>
     *
     * <em>Note:</em> Generally there is no need to put members in a class. However, you can put caches if required.<br>
     * <em>Note 2:</em> Keep a no-args constructor to be used by the {@link DataBuilderFactory}. If, however, you plan
     * to register instances instead of builders in {@link com.flipkart.databuilderframework.engine.DataFlowBuilder}
     * instead of classes, then this is not required.<br>
     * <em>Note 3:</em> The {@link com.flipkart.databuilderframework.model.Data} available in the
     * {@link com.flipkart.databuilderframework.model.DataSet} present in the passed context is scoped to whatever has
     * been mentioned as consumes in {@link com.flipkart.databuilderframework.annotations.DataBuilderInfo}
     * or {@link com.flipkart.databuilderframework.annotations.DataBuilderClassInfo} annotation for this builder.
     *
     * @param context The context object that contains the {@link com.flipkart.databuilderframework.model.DataSet} available for this operation.
     * @return Returns the {@link com.flipkart.databuilderframework.model.Data} to generated.
     *         It will be added to the active working data-set by the system. You should return null if somehow you are
     *         unable to generate data. The execution will stop and the already generated data will be returned.
     *         All data not declared as transient will be added to the {@link com.flipkart.databuilderframework.model.DataSet}
     *         as well.
     * @throws com.flipkart.databuilderframework.engine.DataBuilderException in case any downstream system or itself errors out.
     * (This is generic, as the upper layers need to be prepared for it).
     */
    abstract public Data process(final DataBuilderContext context) throws DataBuilderException,DataValidationException;

    public DataBuilderMeta getDataBuilderMeta() {
        return dataBuilderMeta;
    }

    public void setDataBuilderMeta(DataBuilderMeta dataBuilderMeta) {
        this.dataBuilderMeta = dataBuilderMeta;
    }
}
