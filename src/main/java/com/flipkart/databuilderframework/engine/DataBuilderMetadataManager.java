package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.model.DataBuilderMeta;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Metadata manager class for {@link DataBuilder} implementations.
 * Data stored here is used by {@link DataFlowBuilder} and
 * {@link DataFlowExecutor} classes for building and executing
 * data-flows respectively.
 */
public class DataBuilderMetadataManager {
    private Map<String, Class<? extends DataBuilder>> dataBuilders = Maps.newHashMap();
    private Map<String, DataBuilderMeta> meta = Maps.newHashMap();
    private Map<String, List<DataBuilderMeta>> producedToProducerMap = Maps.newHashMap();
    private Map<String, TreeSet<DataBuilderMeta>> consumesMeta = Maps.newHashMap();

    /**
     * Register metadata for a {@link DataBuilder} implementation.
     * @param consumes List of {@link com.flipkart.databuilderframework.model.Data} this builder consumes
     * @param produces {@link com.flipkart.databuilderframework.model.Data} produced by this builder
     * @param builder Name for this builder. there is no namespacing. Name needs to be unique
     * @param dataBuilder The class of the builder to be created
     * @throws DataFrameworkException In case of name conflict
     */
    public void register(Set<String> consumes, String produces,
                         String builder, Class<? extends DataBuilder> dataBuilder) throws DataFrameworkException {
        DataBuilderMeta metadata = new DataBuilderMeta(consumes, produces, builder);
        if(meta.containsKey(builder)) {
            throw new DataFrameworkException(DataFrameworkException.ErrorCode.BUILDER_EXISTS,
                            "A builder with name " + builder + " already exists");
        }
        meta.put(builder, metadata);
        if(!producedToProducerMap.containsKey(produces)) {
            producedToProducerMap.put(produces, Lists.<DataBuilderMeta>newArrayList());
        }
        producedToProducerMap.get(produces).add(metadata);
        for(String consumesData : consumes) {
            if(!consumesMeta.containsKey(consumesData)) {
                consumesMeta.put(consumesData, Sets.<DataBuilderMeta>newTreeSet());
            }
            consumesMeta.get(consumesData).add(metadata);
        }
        dataBuilders.put(builder, dataBuilder);
    }

    /**
     * Get {@link com.flipkart.databuilderframework.model.DataBuilderMeta} meta for all builders that consume this data.
     * @param data Name of the data to be consumed
     * @return Set of {@link com.flipkart.databuilderframework.model.DataBuilderMeta} for matching builders
     *         or null if none found
     */
    public Set<DataBuilderMeta> getConsumesSetFor(final String data) {
        return consumesMeta.get(data);
    }

    /**
     * Get {@link com.flipkart.databuilderframework.model.DataBuilderMeta} for builder that produces this data
     * @param data Data being produced
     * @return List of {@link com.flipkart.databuilderframework.model.DataBuilderMeta} that are capable of
     *         producing this data or null if not found.
     */
    public List<DataBuilderMeta> getMetaForProducerOf(final String data) {
        return producedToProducerMap.get(data);
    }

    /**
     * Get {@link com.flipkart.databuilderframework.model.DataBuilderMeta} for a particular builder.
     * @param builderName Name of the builder
     * @return Meta if found, null otherwise
     */
    public DataBuilderMeta get(String builderName) {
        return meta.get(builderName);
    }

    /**
     * Get a derived class of {@link DataBuilder} for the given name.
     * @param builderName Name of the builder
     * @return Class if found, null otherwise
     */
    public Class<? extends DataBuilder> getDataBuilderClass(String builderName) {
        return dataBuilders.get(builderName);
    }
}
