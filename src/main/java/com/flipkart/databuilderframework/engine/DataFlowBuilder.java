package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.engine.impl.MixedDataBuilderFactory;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataAdapter;
import com.flipkart.databuilderframework.model.DataBuilderMeta;
import com.flipkart.databuilderframework.model.DataFlow;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Build {@link com.flipkart.databuilderframework.model.DataFlow} for execution by the {@link com.flipkart.databuilderframework.engine.DataFlowExecutor}.
 * A {@link com.flipkart.databuilderframework.model.DataFlow} needs to be calculated based on the target
 * {@link com.flipkart.databuilderframework.model.Data} that needs to be generated. DataFlowBuilder uses the registered
 * {@link com.flipkart.databuilderframework.engine.DataBuilder} classes or instances to find out the {@link com.flipkart.databuilderframework.model.ExecutionGraph}.
 * The execution graph is generated based on the "produces" and "consumes" spec on a builder. This information can be
 * extracted from the {@link com.flipkart.databuilderframework.annotations.DataBuilderInfo} or {@link com.flipkart.databuilderframework.annotations.DataBuilderClassInfo} annotations
 * on the builder class, or can be passed directly while registering the builders. <br>
 * Please note that the consumes and produces information is used to validate the output and scope the inputs to the builders respectively.
 *
 * <br>An object of this type should not be re-used.
 */
public class DataFlowBuilder {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
    private DataFlow dataFlow = new DataFlow();
    private MixedDataBuilderFactory dataBuilderFactory = new MixedDataBuilderFactory();

    public DataFlowBuilder() {
        dataFlow.setTransients(Sets.newHashSet());
    }

    /**
     * Set name for the data flow. This is optional but recommended.
     * @param name Name for the dataflow
     * @return
     */
    public DataFlowBuilder withName(final String name) {
        this.dataFlow.setName(name);
        return this;
    }
    /**
     * Register your own metadata manager. This comes in handy if you want to manage DataBuilder metadata yourself.
     * @param dataBuilderMetadataManager
     * @return
     */
    public DataFlowBuilder withMetaDataManager(DataBuilderMetadataManager dataBuilderMetadataManager) {
        this.dataBuilderMetadataManager = dataBuilderMetadataManager;
        return this;
    }

    /**
     * Register an unnamed,  unannotated builder class.
     * @param produces Name of the data that this builder produces.
     * @param consumes Names of the data that this class consumes.
     * @param dataBuilder Builder class to be used. Class must have a no-args constructor.
     * @return
     * @throws DataBuilderFrameworkException
     */
    public DataFlowBuilder withDataBuilder(String produces,
                                           Set<String> consumes,
                                           Class<? extends DataBuilder> dataBuilder) throws DataBuilderFrameworkException {
        return withDataBuilder(dataBuilder.getSimpleName(), produces, consumes, dataBuilder);
    }

    /**
     * Register a named, unannotated builder class.
     * @param produces Name of the data that this builder produces.
     * @param consumes Names of the data that this class consumes.
     * @param dataBuilder Builder class to be used. Class must have a no-args constructor.
     * @return
     * @throws DataBuilderFrameworkException
     */
    public DataFlowBuilder withDataBuilder(String name,
                                           String produces,
                                           Set<String> consumes,
                                           Class<? extends DataBuilder> dataBuilder) throws DataBuilderFrameworkException {
        return withDataBuilder(name, produces, consumes, dataBuilder, false);
    }

    /**
     * Register a unannotated builder class.
     * @param produces Name of the data that this builder produces.
     * @param consumes Names of the data that this class consumes.
     * @param dataBuilder Builder class to be used. Class must have a no-args constructor.
     * @param isTransient Data produced by this class is transient and will not be a part of the data-set.
     * @return
     * @throws DataBuilderFrameworkException
     */
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

    /**
     * Register a builder class annotated with either {@link com.flipkart.databuilderframework.annotations.DataBuilderInfo} or {@link com.flipkart.databuilderframework.annotations.DataBuilderClassInfo}
     * @return
     * @throws DataBuilderFrameworkException
     */

    public DataFlowBuilder withAnnotatedDataBuilder(Class<? extends DataBuilder> annotatedDataBuilder) throws DataBuilderFrameworkException {
        dataBuilderMetadataManager.register(annotatedDataBuilder);
        return this;
    }

    /**
     * Register an unnamed, unannotated builder instance.
     * @param produces Name of the data that this builder produces.
     * @param consumes Names of the data that this builder consumes.
     * @param dataBuilder Builder class to be used.
     * @return
     * @throws DataBuilderFrameworkException
     */
    public DataFlowBuilder withDataBuilder(String produces,
                                           Set<String> consumes,
                                           DataBuilder dataBuilder) throws DataBuilderFrameworkException {
        return withDataBuilder(dataBuilder.getClass().getSimpleName(), produces, consumes, dataBuilder);
    }

    /**
     * Register a named, unannotated builder instance.
     * @param produces Name of the data that this builder produces.
     * @param consumes Names of the data that this builder consumes.
     * @param dataBuilder Builder class to be used.
     * @return
     * @throws DataBuilderFrameworkException
     */
    public DataFlowBuilder withDataBuilder(String name,
                                           String produces,
                                           Set<String> consumes,
                                           DataBuilder dataBuilder) throws DataBuilderFrameworkException {
        return withDataBuilder(name, produces, consumes, dataBuilder, false);
    }

    /**
     * Register a unannotated builder instance.
     * @param produces Name of the data that this builder produces.
     * @param consumes Names of the data that this builder consumes.
     * @param dataBuilder Builder class to be used.
     * @param isTransient Data produced by this class is transient and will not be a part of the data-set.
     * @return
     * @throws DataBuilderFrameworkException
     */
    public DataFlowBuilder withDataBuilder(String name,
                                           String produces,
                                           Set<String> consumes,
                                           DataBuilder dataBuilder,
                                           boolean isTransient) throws DataBuilderFrameworkException {
        DataBuilderMeta dataBuilderMeta = new DataBuilderMeta(consumes, produces, name);
        if(isTransient) {
            dataFlow.getTransients().add(name);
        }
        dataBuilderMetadataManager.register(dataBuilderMeta, dataBuilder.getClass());
        dataBuilderFactory.register(new ProxyDataBuilder(dataBuilderMeta, dataBuilder));
        return this;
    }

    /**
     * Regsiter an instance of a builder annotated with either {@link com.flipkart.databuilderframework.annotations.DataBuilderInfo}
     * or {@link com.flipkart.databuilderframework.annotations.DataBuilderClassInfo}
     * @param dataBuilder Builder instance
     * @return
     * @throws DataBuilderFrameworkException
     */
    public DataFlowBuilder withDataBuilder(DataBuilder dataBuilder) throws DataBuilderFrameworkException {
        Preconditions.checkNotNull(dataBuilder, "Null DataBuilder cannot be registered");
        DataBuilderMeta dataBuilderMeta = Utils.meta(dataBuilder);
        Preconditions.checkNotNull(dataBuilderMeta,
                "No useful annotations found on class. Use DataBuilderInfo or DataBuilderClassInfo to annotate");
        dataBuilderMetadataManager.register(dataBuilderMeta, dataBuilder.getClass());
        dataBuilderFactory.register(new ProxyDataBuilder(dataBuilderMeta, dataBuilder));
        return this;
    }

    /**
     * The data to be generated. The build method will use this to create the execution graph.
     * @param targetDataClass Class name for the data to be generated.
     * @return
     */
    public DataFlowBuilder withTargetData(Class<? extends DataAdapter> targetDataClass) {
        this.dataFlow.setTargetData(Utils.name(targetDataClass));
        return this;
    }

    /**
     * The data to be generated. The build method will use this to create the execution graph.
     * @param data Logical name for the data to be generated.
     * @return
     */
    public DataFlowBuilder withTargetData(String data) {
        this.dataFlow.setTargetData(data);
        return this;
    }

    /**
     * Register a resolution spec to resolve conflicts in scenarios when multiple builders known to the system can generate the same required data.
     * @param data Data to be generated
     * @param generatingBuilder Name of the builder that will generate this data in the context of the flow being built
     * @return
     */
    public DataFlowBuilder withResolutionSpec(final String data, final String generatingBuilder) {
        this.dataFlow.getResolutionSpecs().put(data, generatingBuilder);
        return this;
    }

    /**
     * Register a resolution spec to resolve conflicts in scenarios when multiple builders known to the system can generate the same required data.
     * @param data Data to be generated
     * @param generatingBuilder Name of the builder that will generate this data in the context of the flow being built
     * @return
     */
    public DataFlowBuilder withResolutionSpec(final Class<? extends Data> data, final Class<? extends DataBuilder> generatingBuilder) {
        this.dataFlow.getResolutionSpecs().put(Utils.name(data), Utils.name(generatingBuilder));
        return this;
    }

    /**
     * Register a transient data
     *
     * @param data Name of transient data
     * @return
     */
    public DataFlowBuilder withTransientData(String data) {
        dataFlow.getTransients().add(data);
        return this;
    }

    /**
     * Register a transient data by class name
     *
     * @param dataClass Class of the data. Name will be derived from this
     * @return
     */
    public DataFlowBuilder withTransientDataClass(Class<? extends Data> dataClass) {
        dataFlow.getTransients().add(Utils.name(dataClass));
        return this;
    }

    /**
     * Register a transient data
     *
     * @param data Names of transient data
     * @return
     */
    public DataFlowBuilder withTransientData(Collection<String> data) {
        dataFlow.getTransients().addAll(data);
        return this;
    }

    /**
     * Register a transient data by class name
     *
     * @param dataClasses Classes of the data. Name will be derived from this
     * @return
     */
    public DataFlowBuilder withTransientDataClasses(Collection<Class<? extends Data>> dataClasses) {
        dataFlow.getTransients().addAll(
                dataClasses.stream()
                        .map(Utils::name)
                        .collect(Collectors.toList()));
        return this;
    }

    /**
     * Build the data flow.
     */
     public DataFlow build() throws DataBuilderFrameworkException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(dataFlow.getTargetData()), "Specify target data");
        dataBuilderFactory.setDataBuilderMetadataManager(dataBuilderMetadataManager);
        dataFlow.setExecutionGraph(new ExecutionGraphGenerator(dataBuilderMetadataManager).generateGraph(dataFlow));
        dataFlow.setDataBuilderFactory(dataBuilderFactory);
        return dataFlow;
    }
}
