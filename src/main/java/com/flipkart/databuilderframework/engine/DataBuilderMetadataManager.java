package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataBuilderMeta;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.*;

import java.util.*;

/**
 * Metadata manager class for {@link DataBuilder} implementations.
 * Data stored here is used by {@link ExecutionGraphGenerator} and
 * {@link DataFlowExecutor} classes for building and executing
 * data-flows respectively.
 */
public class DataBuilderMetadataManager {
    private Map<String, Class<? extends DataBuilder>> dataBuilders = Maps.newHashMap();
    private Map<String, DataBuilderMeta> meta = Maps.newHashMap();
    private Map<String, List<DataBuilderMeta>> producedToProducerMap = Maps.newHashMap();
    private Map<String, TreeSet<DataBuilderMeta>> consumesMeta = Maps.newHashMap();
    private Map<String, TreeSet<DataBuilderMeta>> optionalsMeta = Maps.newHashMap();
	private Map<String, TreeSet<DataBuilderMeta>> accessesMeta = Maps.newHashMap();
	
    private DataBuilderMetadataManager(Map<String, Class<? extends DataBuilder>> dataBuilders,
                                       Map<String, DataBuilderMeta> meta,
                                       Map<String, List<DataBuilderMeta>> producedToProducerMap,
                                       Map<String, TreeSet<DataBuilderMeta>> consumesMeta,
                                       Map<String, TreeSet<DataBuilderMeta>> optionalsMeta,
                           				Map<String, TreeSet<DataBuilderMeta>> accessesMeta) {
        this.dataBuilders = dataBuilders;
        this.meta = meta;
        this.producedToProducerMap = producedToProducerMap;
        this.consumesMeta = consumesMeta;
		this.optionalsMeta = optionalsMeta;
		this.accessesMeta = accessesMeta;
    }

    public DataBuilderMetadataManager() {
    }

    public DataBuilderMetadataManager register(Class<? extends DataBuilder> annotatedDataBuilder) throws DataBuilderFrameworkException {
        DataBuilderInfo info = annotatedDataBuilder.getAnnotation(DataBuilderInfo.class);
        if(null != info) {
            register(
                    ImmutableSet.copyOf(info.consumes()),
                    ImmutableSet.copyOf(info.optionals()),
					ImmutableSet.copyOf(info.accesses()),
                    info.produces(),
                    info.name(),
                    annotatedDataBuilder);
        }
        else {
            DataBuilderClassInfo dataBuilderClassInfo = annotatedDataBuilder.getAnnotation(DataBuilderClassInfo.class);
            Preconditions.checkNotNull(dataBuilderClassInfo,
                    "No useful annotations found on class. Use DataBuilderInfo or DataBuilderClassInfo to annotate");
            Set<String> consumes = Sets.newHashSet();
            Set<String> optionals = Sets.newHashSet();
			Set<String> access = Sets.newHashSet();
			//TODO (gokul) remove getCannonicalName() and inject a Handler for client to customize this

            for(Class<? extends Data> data : dataBuilderClassInfo.consumes()) {
                consumes.add(data.getCanonicalName());
            }
            
            for(Class<? extends Data> data : dataBuilderClassInfo.optionals()) {
				optionals.add(data.getCanonicalName());
			}

			for(Class<? extends Data> data : dataBuilderClassInfo.accesses()) {
				access.add(data.getCanonicalName());
			}

            register(
                    ImmutableSet.copyOf(consumes),
                	ImmutableSet.copyOf(optionals),
					ImmutableSet.copyOf(access),
                    dataBuilderClassInfo.produces().getCanonicalName(),
                    Strings.isNullOrEmpty(dataBuilderClassInfo.name())
                            ? annotatedDataBuilder.getCanonicalName()
                            : dataBuilderClassInfo.name(),
                    annotatedDataBuilder);
        }
        return this;
    }

    /**
     * Register builder by using meta directly.
     *
     * @param dataBuilderMeta Meta about the builder
     * @param dataBuilder The actual databuilder class
     * @return this
     * @throws DataBuilderFrameworkException
     */
    public DataBuilderMetadataManager register(DataBuilderMeta dataBuilderMeta, Class<? extends DataBuilder> dataBuilder) throws DataBuilderFrameworkException {
        return register(dataBuilderMeta.getConsumes(), dataBuilderMeta.getOptionals(), dataBuilderMeta.getAccess(), dataBuilderMeta.getProduces(), dataBuilderMeta.getName(), dataBuilder);
    }

    /**
     * Register metadata for a {@link DataBuilder} implementation.
     * @param consumes List of {@link com.flipkart.databuilderframework.model.Data} this builder consumes
     * @param produces {@link com.flipkart.databuilderframework.model.Data} produced by this builder
     * @param builder Name for this builder. there is no namespacing. Name needs to be unique
     * @param dataBuilder The class of the builder to be created
     * @throws DataBuilderFrameworkException In case of name conflict
     */
    public DataBuilderMetadataManager register(Set<String> consumes, String produces,
                         String builder, Class<? extends DataBuilder> dataBuilder) throws DataBuilderFrameworkException {
    	return register(consumes, null, null, produces, builder, dataBuilder);
    }

    public DataBuilderMetadataManager registerWithOptionals(Set<String> consumes, Set<String> optionals, String produces,
			String builder, Class<? extends DataBuilder> dataBuilder) throws DataBuilderFrameworkException {
		return register(consumes, optionals, null, produces, builder, dataBuilder);
	}

	public DataBuilderMetadataManager registerWithAccess(Set<String> consumes, Set<String> access, String produces,
			String builder, Class<? extends DataBuilder> dataBuilder) throws DataBuilderFrameworkException {
		return register(consumes, null, access, produces, builder, dataBuilder);
	}
	
    public DataBuilderMetadataManager register(Set<String> consumes, Set<String> optionals, Set<String> access, String produces,
			String builder, Class<? extends DataBuilder> dataBuilder) throws DataBuilderFrameworkException {
		DataBuilderMeta metadata = new DataBuilderMeta(consumes, produces, builder, optionals, access);
		if(meta.containsKey(builder)) {
			throw new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.BUILDER_EXISTS,
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

		if(optionals != null){
			for(String optionalsData : optionals) {
				if(!optionalsMeta.containsKey(optionalsData)) {
					optionalsMeta.put(optionalsData, Sets.<DataBuilderMeta>newTreeSet());
				}
				optionalsMeta.get(optionalsData).add(metadata);
			}

		}
		if(access != null){
			for(String accessData : access) {
				if(!accessesMeta.containsKey(accessData)) {
					accessesMeta.put(accessData, Sets.<DataBuilderMeta>newTreeSet());
				}
				accessesMeta.get(accessData).add(metadata);
			}
		}
		dataBuilders.put(builder, dataBuilder);
		return this;
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
     * Get all {@link com.flipkart.databuilderframework.model.DataBuilderMeta} for the for the provided names.
     * @param builderNames List of data builder names
     * @return List of builder metadata
     */
    public Collection<DataBuilderMeta> get(List<String> builderNames) {
        return Maps.filterKeys(meta, Predicates.in(builderNames)).values();
    }

    public boolean contains(List<String> builderNames) {
        return meta.keySet().containsAll(builderNames);
    }

    /**
     * Get a derived class of {@link DataBuilder} for the given name.
     * @param builderName Name of the builder
     * @return Class if found, null otherwise
     */
    public Class<? extends DataBuilder> getDataBuilderClass(String builderName) {
        return dataBuilders.get(builderName);
    }

    public DataBuilderMetadataManager immutableCopy() {
        return new DataBuilderMetadataManager(ImmutableMap.copyOf(dataBuilders),
        		
                ImmutableMap.copyOf(meta),
                ImmutableMap.copyOf(producedToProducerMap),
                ImmutableMap.copyOf(consumesMeta),
                ImmutableMap.copyOf(optionalsMeta),
				ImmutableMap.copyOf(accessesMeta));
    }
}