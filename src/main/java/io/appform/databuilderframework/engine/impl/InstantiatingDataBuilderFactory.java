package io.appform.databuilderframework.engine.impl;

import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderFactory;
import io.appform.databuilderframework.engine.DataBuilderFrameworkException;
import io.appform.databuilderframework.engine.DataBuilderMetadataManager;
import io.appform.databuilderframework.model.DataBuilderMeta;

/**
 * @inheritDoc
 * This particular version, uses metadata stored in {@link io.appform.databuilderframework.engine.DataBuilderMetadataManager}
 * to generate a specific builder.
 */
public class InstantiatingDataBuilderFactory implements DataBuilderFactory {
    private DataBuilderMetadataManager dataBuilderMetadataManager;
    private boolean useCurrentMeta;

    public InstantiatingDataBuilderFactory(DataBuilderMetadataManager dataBuilderMetadataManager) {
        this(dataBuilderMetadataManager, false);
    }

    public InstantiatingDataBuilderFactory(DataBuilderMetadataManager dataBuilderMetadataManager, boolean useCurrentMeta) {
        this.dataBuilderMetadataManager = dataBuilderMetadataManager;
        this.useCurrentMeta = useCurrentMeta;
    }

    public DataBuilder create(DataBuilderMeta dataBuilderMeta) throws DataBuilderFrameworkException {
        final String builderName = dataBuilderMeta.getName();
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

    @Override
    public DataBuilderFactory immutableCopy() {
        return new InstantiatingDataBuilderFactory(dataBuilderMetadataManager.immutableCopy());
    }
}
