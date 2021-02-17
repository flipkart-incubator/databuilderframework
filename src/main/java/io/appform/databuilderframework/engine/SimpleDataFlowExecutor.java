package io.appform.databuilderframework.engine;

import io.appform.databuilderframework.model.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The executor for a {@link io.appform.databuilderframework.model.DataFlow}.
 */
public class SimpleDataFlowExecutor extends DataFlowExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SimpleDataFlowExecutor.class.getSimpleName());

    public SimpleDataFlowExecutor() {

    }

    public SimpleDataFlowExecutor(DataBuilderFactory dataBuilderFactory) {
        super(dataBuilderFactory);
    }

    /*
     * {@inheritDoc}
     */
    protected DataExecutionResponse run(DataBuilderContext dataBuilderContext,
                                        DataFlowInstance dataFlowInstance,
                                        DataDelta dataDelta,
                                        DataFlow dataFlow,
                                        DataBuilderFactory builderFactory) throws DataBuilderFrameworkException, DataValidationException {
        ExecutionGraph executionGraph = dataFlow.getExecutionGraph();
        DataSet dataSet = dataFlowInstance.getDataSet().accessor().copy(); //Create own copy to work with
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        dataSetAccessor.merge(dataDelta);
        Map<String, Data> responseData = Maps.newTreeMap();
        Set<String> activeDataSet = Sets.newHashSet();

        activeDataSet.addAll(dataDelta.getDelta()
                .stream()
                .map(Data::getData)
                .collect(Collectors.toList()));
        List<List<DataBuilderMeta>> dependencyHierarchy = executionGraph.getDependencyHierarchy();
        Set<String> newlyGeneratedData = Sets.newHashSet();
        Set<DataBuilderMeta> processedBuilders = Sets.newHashSet();
        while(true) {
            for (List<DataBuilderMeta> levelBuilders : dependencyHierarchy) {
                for (DataBuilderMeta builderMeta : levelBuilders) {
                    if (processedBuilders.contains(builderMeta)) {
                        continue;
                    }
                    //If there is an intersection, means some of it's inputs have changed. Reevaluate
                    if (Sets.intersection(builderMeta.getEffectiveConsumes(), activeDataSet).isEmpty()) {
                        continue;
                    }
                    DataBuilder builder = builderFactory.create(builderMeta);
                    if (!dataSetAccessor.checkForData(builder.getDataBuilderMeta().getConsumes())) {
                        continue;
                    }
                    for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                        try {
                            listener.beforeExecute(dataBuilderContext, dataFlowInstance, builderMeta, dataDelta, responseData);
                        } catch (Throwable t) {
                            logger.error("Error running pre-execution execution listener: ", t);
                        }
                    }
                    try {
                        Data response = builder.process(
                                                    dataBuilderContext.immutableCopy(
                                                            dataSet.accessor().getAccesibleDataSetFor(builder)));
                        if (null != response) {
                            Preconditions.checkArgument(response.getData().equalsIgnoreCase(builderMeta.getProduces()),
                                                String.format("Builder is supposed to produce %s but produces %s",
                                                                builderMeta.getProduces(), response.getData()));
                            dataSetAccessor.merge(response);
                            responseData.put(response.getData(), response);
                            response.setGeneratedBy(builderMeta.getName());
                            activeDataSet.add(response.getData());
                            if(null != dataFlow.getTransients() && !dataFlow.getTransients().contains(response.getData())) {
                                newlyGeneratedData.add(response.getData());
                            }
                        }
                        //logger.debug("Ran " + builderMeta.getName());
                        processedBuilders.add(builderMeta);
                        for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                            try {
                                listener.afterExecute(dataBuilderContext, dataFlowInstance, builderMeta, dataDelta, responseData, response);
                            } catch (Throwable t) {
                                logger.error("Error running post-execution listener: ", t);
                            }
                        }

                    } catch (DataBuilderException e) {
                        logger.error("Error running builder: " + builderMeta.getName());
                        for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                            try {
                                listener.afterException(dataBuilderContext, dataFlowInstance, builderMeta, dataDelta, responseData, e);

                            } catch (Throwable error) {
                                logger.error("Error running post-execution listener: ", error);
                            }
                        }
                        throw new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR,
                                "Error running builder: " + builderMeta.getName(), e.getDetails(), e, new DataExecutionResponse(responseData));

                    } catch (DataValidationException e) {
                        logger.error("Validation error in data produced by builder" +builderMeta.getName());
                        for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                            try {
                                listener.afterException(dataBuilderContext, dataFlowInstance, builderMeta, dataDelta, responseData, e);

                            } catch (Throwable error) {
                                logger.error("Error running post-execution listener: ", error);
                            }
                        }
                        // Sending Execution response in exception object

                        throw new DataValidationException(DataValidationException.ErrorCode.DATA_VALIDATION_EXCEPTION, e.getMessage(), new DataExecutionResponse(responseData),e.getDetails(), e);


                    }
                    catch (Throwable t) {
                        logger.error("Error running builder: " + builderMeta.getName());
                        for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                            try {
                                listener.afterException(dataBuilderContext, dataFlowInstance, builderMeta, dataDelta, responseData, t);

                            } catch (Throwable error) {
                                logger.error("Error running post-execution listener: ", error);
                            }
                        }
                        Map<String, Object> objectMap = new HashMap<String, Object>();
                        objectMap.put("MESSAGE", t.getMessage());
                        throw new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR,
                                "Error running builder: " + builderMeta.getName()
                                        + ": " + t.getMessage(), objectMap, t, new DataExecutionResponse(responseData));
                    }
                }
            }
            if(newlyGeneratedData.contains(dataFlow.getTargetData())) {
                //logger.debug("Finished running this instance of the flow. Exiting.");
                break;
            }
            if(newlyGeneratedData.isEmpty()) {
                //logger.debug("Nothing happened in this loop, exiting..");
                break;
            }
//            StringBuilder stringBuilder = new StringBuilder();
//            for(String data : newlyGeneratedData) {
//                stringBuilder.append(data + ", ");
//            }
            //logger.info("Newly generated: " + stringBuilder);
            activeDataSet.clear();
            activeDataSet.addAll(newlyGeneratedData);
            newlyGeneratedData.clear();
            if(!dataFlow.isLoopingEnabled()) {
                break;
            }
        }
        DataSet finalDataSet = dataSetAccessor.copy(dataFlow.getTransients());
        dataFlowInstance.setDataSet(finalDataSet);
        return new DataExecutionResponse(responseData);
    }

}
