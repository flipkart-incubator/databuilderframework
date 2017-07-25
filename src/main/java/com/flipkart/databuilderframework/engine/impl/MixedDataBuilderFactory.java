package com.flipkart.databuilderframework.engine.impl;

import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderFactory;
import com.flipkart.databuilderframework.engine.DataBuilderFrameworkException;
import com.flipkart.databuilderframework.engine.DataBuilderMetadataManager;
import com.flipkart.databuilderframework.model.DataBuilderMeta;
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
    private boolean useCurrentMeta;

    public MixedDataBuilderFactory() {
    }

    public MixedDataBuilderFactory(
            Map<String, DataBuilder> builderInstances,
            DataBuilderMetadataManager dataBuilderMetadataManager,
            boolean useCurrentMeta) {
        this.builderInstances = builderInstances;
        this.dataBuilderMetadataManager = dataBuilderMetadataManager;
        this.useCurrentMeta = useCurrentMeta;
    }

    public void setDataBuilderMetadataManager(DataBuilderMetadataManager dataBuilderMetadataManager) {
        this.dataBuilderMetadataManager = dataBuilderMetadataManager;
    }

    public void register(DataBuilder dataBuilder) {
        builderInstances.put(dataBuilder.getDataBuilderMeta().getName(), dataBuilder);
    }

    public DataBuilder create(DataBuilderMeta dataBuilderMeta) throws DataBuilderFrameworkException {
        final String builderName = dataBuilderMeta.getName();
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
            if(useCurrentMeta) {
                dataBuilder.setDataBuilderMeta(dataBuilderMetadataManager.get(builderName).deepCopy());
            }
            else {
                dataBuilder.setDataBuilderMeta(dataBuilderMeta.deepCopy());
            }
            return dataBuilder;
        } catch (Exception e) {
            throw new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.INSTANTIATION_FAILURE,
                                    "Could not instantiate builder: " + builderName);
        }
    }

    public MixedDataBuilderFactory immutableCopy() {
        return new MixedDataBuilderFactory(ImmutableMap.copyOf(builderInstances),
                                            dataBuilderMetadataManager.immutableCopy(),
                                            useCurrentMeta);
    }
}
