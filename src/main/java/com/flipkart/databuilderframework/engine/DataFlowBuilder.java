package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataFlow;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

public class DataFlowBuilder {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlow dataFlow = new DataFlow();
    private DataBuilderFactory dataBuilderFactory;

    public DataFlowBuilder() {
        dataFlow.setTransients(Sets.<String>newHashSet());
    }

    public DataFlowBuilder withMetaDataManager(DataBuilderMetadataManager dataBuilderMetadataManager) {
        this.dataBuilderMetadataManager = dataBuilderMetadataManager;
        return this;
    }

    public DataFlowBuilder withDataBuilder(String produces,
                                                         Set<String> consumes,
                                                         Class<? extends DataBuilder> dataBuilder) throws DataBuilderFrameworkException {
        return withDataBuilder(dataBuilder.getSimpleName(), produces, consumes, dataBuilder);
    }

    public DataFlowBuilder withDataBuilder(String name,
                                                         String produces,
                                                         Set<String> consumes,
                                                         Class<? extends DataBuilder> dataBuilder) throws DataBuilderFrameworkException {
        return withDataBuilder(name, produces, consumes, dataBuilder, false);
    }

    public DataFlowBuilder withDataBuilder(String name,
                                                         String produces,
                                                         Set<String> consumes,
                                                         Class<? extends DataBuilder> dataBuilder,
                                                         boolean isTransient) throws DataBuilderFrameworkException {
        dataBuilderMetadataManager.register(consumes, produces, name, dataBuilder);
        if(isTransient) {
            dataFlow.getTransients().add(name);
        }
        return this;
    }

    public DataFlowBuilder withAnnotatedDataBuilder(Class<? extends DataBuilder> annotatedDataBuilder) throws DataBuilderFrameworkException {
        DataBuilderInfo info = annotatedDataBuilder.getAnnotation(DataBuilderInfo.class);
        if(null != info) {
            dataBuilderMetadataManager.register(
                    ImmutableSet.copyOf(info.consumes()),
                    info.produces(),
                    info.name(),
                    annotatedDataBuilder);
        }
        else {
            DataBuilderClassInfo dataBuilderClassInfo = annotatedDataBuilder.getAnnotation(DataBuilderClassInfo.class);
            Preconditions.checkNotNull(dataBuilderClassInfo,
                    "No useful annotations found on class. Use DataBuilderInfo or DataBuilderClassInfo to annotate");
            Set<String> consumes = Sets.newHashSet();
            for(Class<? extends Data> data : dataBuilderClassInfo.consumes()) {
                consumes.add(data.getCanonicalName());
            }
            dataBuilderMetadataManager.register(
                    ImmutableSet.copyOf(consumes),
                    dataBuilderClassInfo.produces().getCanonicalName(),
                    Strings.isNullOrEmpty(dataBuilderClassInfo.name())
                            ? annotatedDataBuilder.getCanonicalName()
                            : dataBuilderClassInfo.name(),
                    annotatedDataBuilder);
        }
        return this;
    }

    public DataFlowBuilder withDataBuilderFactory(DataBuilderFactory dataBuilderFactory) {
        this.dataBuilderFactory = dataBuilderFactory;
        return this;
    }

    public DataFlowBuilder withTargetData(Class<? extends Data> targetDataClass) {
        this.dataFlow.setTargetData(targetDataClass.getCanonicalName());
        return this;
    }

    public DataFlowBuilder withTargetData(String data) {
        this.dataFlow.setTargetData(data);
        return this;
    }

    public DataFlow build() throws DataBuilderFrameworkException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(dataFlow.getTargetData()), "Specify target data");
        if(null == dataBuilderFactory) {
            dataBuilderFactory = new InstantiatingDataBuilderFactory(dataBuilderMetadataManager);
        }
        dataFlow.setExecutionGraph(new ExecutionGraphGenerator(dataBuilderMetadataManager).generateGraph(dataFlow));
        dataFlow.setDataBuilderFactory(dataBuilderFactory);
        return dataFlow;
    }
}
