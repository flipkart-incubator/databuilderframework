package com.flipkart.databuilderframework.engine.impl;

import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderFactory;
import com.flipkart.databuilderframework.engine.DataBuilderMetadataManager;
import com.flipkart.databuilderframework.engine.DataFrameworkException;

/**
 * @inheritDoc
 * This particular version, uses metadata stored in {@link com.flipkart.databuilderframework.engine.DataBuilderMetadataManager}
 * to generate a specific builder.
 */
public class DataBuilderFactoryImpl implements DataBuilderFactory {
    private DataBuilderMetadataManager dataBuilderMetadataManager;

    public DataBuilderFactoryImpl(DataBuilderMetadataManager dataBuilderMetadataManager) {
        this.dataBuilderMetadataManager = dataBuilderMetadataManager;
    }

    public DataBuilder create(String builderName) throws DataFrameworkException {
        Class<? extends DataBuilder> dataBuilderClass = dataBuilderMetadataManager.getDataBuilderClass(builderName);
        if(null == dataBuilderClass) {
            throw new DataFrameworkException(DataFrameworkException.ErrorCode.NO_BUILDER_FOUND_FOR_NAME,
                                    "No builder found for name: " + builderName);
        }
        try {
            DataBuilder dataBuilder = dataBuilderClass.newInstance();
            dataBuilder.setDataBuilderMeta(dataBuilderMetadataManager.get(builderName).deepCopy());
            return dataBuilder;
        } catch (Exception e) {
            throw new DataFrameworkException(DataFrameworkException.ErrorCode.INSTANTIATION_FAILURE,
                                    "Could not instantiate builder: " + builderName);
        }
    }
}
