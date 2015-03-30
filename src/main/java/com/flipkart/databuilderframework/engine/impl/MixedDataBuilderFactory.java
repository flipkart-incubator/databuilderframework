package com.flipkart.databuilderframework.engine.impl;

import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderFactory;
import com.flipkart.databuilderframework.engine.DataBuilderFrameworkException;
import com.flipkart.databuilderframework.engine.DataBuilderMetadataManager;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @inheritDoc
 * This particular version, uses metadata stored in {@link com.flipkart.databuilderframework.engine.DataBuilderMetadataManager}
 * to generate a specific builder.
 */
public class MixedDataBuilderFactory implements DataBuilderFactory {
    private Map<String, DataBuilder> builderInstances = Maps.newHashMap();
    private DataBuilderMetadataManager dataBuilderMetadataManager;

    public MixedDataBuilderFactory() {
    }

    public MixedDataBuilderFactory(Map<String, DataBuilder> builderInstances, DataBuilderMetadataManager dataBuilderMetadataManager) {
        this.builderInstances = builderInstances;
        this.dataBuilderMetadataManager = dataBuilderMetadataManager;
    }

    public void setDataBuilderMetadataManager(DataBuilderMetadataManager dataBuilderMetadataManager) {
        this.dataBuilderMetadataManager = dataBuilderMetadataManager;
    }

    public void register(DataBuilder dataBuilder) {
        builderInstances.put(dataBuilder.getDataBuilderMeta().getName(), dataBuilder);
    }

    public DataBuilder create(String builderName) throws DataBuilderFrameworkException {
        if(builderInstances.containsKey(builderName)) {
            return builderInstances.get(builderName);
        }
        Class<? extends DataBuilder> dataBuilderClass = dataBuilderMetadataManager.getDataBuilderClass(builderName);
        if(null == dataBuilderClass) {
            throw new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.NO_BUILDER_FOUND_FOR_NAME,
                                    "No builder found for name: " + builderName);
        }
        try {
            DataBuilder dataBuilder = dataBuilderClass.newInstance();
            dataBuilder.setDataBuilderMeta(dataBuilderMetadataManager.get(builderName).deepCopy());
            return dataBuilder;
        } catch (Exception e) {
            throw new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.INSTANTIATION_FAILURE,
                                    "Could not instantiate builder: " + builderName);
        }
    }

    public MixedDataBuilderFactory immutableCopy() {
        return new MixedDataBuilderFactory(ImmutableMap.copyOf(builderInstances),
                                            dataBuilderMetadataManager.immutableCopy());
    }
}
