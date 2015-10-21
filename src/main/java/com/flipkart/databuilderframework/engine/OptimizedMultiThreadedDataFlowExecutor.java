package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.model.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * The executor for a {@link com.flipkart.databuilderframework.model.DataFlow}.
 */
public class OptimizedMultiThreadedDataFlowExecutor extends DataFlowExecutor {
    private static final Logger logger = LoggerFactory.getLogger(OptimizedMultiThreadedDataFlowExecutor.class.getSimpleName());
    private final ExecutorService executorService;

    public OptimizedMultiThreadedDataFlowExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public OptimizedMultiThreadedDataFlowExecutor(DataBuilderFactory dataBuilderFactory, ExecutorService executorService) {
        super(dataBuilderFactory);
        this.executorService = executorService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataExecutionResponse run(DataBuilderContext dataBuilderContext,
                                        DataFlowInstance dataFlowInstance,
                                        DataDelta dataDelta,
                                        DataFlow dataFlow,
                                        DataBuilderFactory builderFactory) throws DataBuilderFrameworkException, DataValidationException {
        CompletionService<DataContainer> completionExecutor = new ExecutorCompletionService<DataContainer>(executorService);
        ExecutionGraph executionGraph = dataFlow.getExecutionGraph();
        DataSet dataSet = dataFlowInstance.getDataSet().accessor().copy(); //Create own copy to work with
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        dataSetAccessor.merge(dataDelta);
        Map<String, Data> responseData = Maps.newTreeMap();
        Set<String> activeDataSet = Sets.newHashSet();

        for (Data data : dataDelta.getDelta()) {
            activeDataSet.add(data.getData());
        }
        List<List<DataBuilderMeta>> dependencyHierarchy = executionGraph.getDependencyHierarchy();
        Set<String> newlyGeneratedData = Sets.newHashSet();
        Set<DataBuilderMeta> processedBuilders = Collections.synchronizedSet(Sets.<DataBuilderMeta>newHashSet());
        while(true) {
            for (List<DataBuilderMeta> levelBuilders : dependencyHierarchy) {
                List<Future<DataContainer>> dataFutures = Lists.newArrayList();
                BuilderRunner singleRef = null; //refrence to builderRunner when size of levelBuilders == 1 to avoid running it behind thread
                for (DataBuilderMeta builderMeta : levelBuilders) {
                    if (processedBuilders.contains(builderMeta)) {
                        continue;
                    }
                    //If there is an intersection, means some of it's inputs have changed. Reevaluate
                    if (Sets.intersection(builderMeta.getEffectiveConsumes(), activeDataSet).isEmpty()) {
                        continue;
                    }
                    DataBuilder builder = builderFactory.create(builderMeta.getName());
                    if (!dataSetAccessor.checkForData(builder.getDataBuilderMeta().getConsumes())) {
                        continue; //No need to run others, list is topo sorted
                    }
                    BuilderRunner builderRunner = new BuilderRunner(dataBuilderExecutionListener, dataFlowInstance,
                                                                        builderMeta, dataDelta, responseData,
                                                                        builder, dataBuilderContext, processedBuilders, dataSet);
                   
                    if(levelBuilders.size() == 1){
                    	singleRef = builderRunner;
                    }else{
	                    Future<DataContainer> future = completionExecutor.submit(builderRunner);
	                    dataFutures.add(future);
                    }
                }

                //Now wait for something to complete.
                int listSize = dataFutures.size();
                for(int i = 0; i < listSize || singleRef != null; i++) { //listSize == 0 when singleRef is present, or condition allows this logic to run once
                    try {
                    	 DataContainer responseContainer = null;
                    	if(singleRef != null){
                    		try {
								responseContainer = singleRef.call();
							} catch (Exception e) {
								throw new ExecutionException(e); //to map this to existing exception handling
							}finally{
								singleRef = null; // make this null to avoid loopback hell
							}
                    	}else{
                    		responseContainer = completionExecutor.take().get();
                    	}
                       
                        Data response = responseContainer.getGeneratedData();
                        if(responseContainer.isHasError()) {
                            if(null != responseContainer.getValidationException()) {
                                throw responseContainer.getValidationException();

                            }

                            throw responseContainer.getException();
                        }
                        if (null != response) {
                            dataSetAccessor.merge(response);
                            responseData.put(response.getData(), response);
                            activeDataSet.add(response.getData());
                            if(null != dataFlow.getTransients() && !dataFlow.getTransients().contains(response.getData())) {
                                newlyGeneratedData.add(response.getData());
                            }
                        }
                    }
                    catch (InterruptedException e) {
                        throw new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR,
                                "Error while waiting for error ", e);
                    } catch (ExecutionException e) {
                        throw new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR,
                                "Error while waiting for error ", e.getCause());
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

    private static final class DataContainer {
        private final DataBuilderMeta builderMeta;
        private final Data generatedData;
        private boolean hasError = false;
        private final DataBuilderFrameworkException exception;
        private final DataValidationException validationException;

        private DataContainer(DataBuilderMeta builderMeta, Data generatedData) {
            this.builderMeta = builderMeta;
            this.generatedData = generatedData;
            this.exception = null;
            this.validationException = null;
        }

        private DataContainer(DataBuilderMeta builderMeta, DataBuilderFrameworkException exception) {
            this.builderMeta = builderMeta;
            this.generatedData = null;
            this.hasError = true;
            this.exception = exception;
            this.validationException = null;
        }

        private DataContainer(DataBuilderMeta builderMeta, DataValidationException validationException) {
            this.builderMeta = builderMeta;
            this.generatedData = null;
            this.hasError = true;
            this.exception = null;
            this.validationException = validationException;
        }



        public DataBuilderMeta getBuilderMeta() {
            return builderMeta;
        }

        public Data getGeneratedData() {
            return generatedData;
        }

        public DataBuilderFrameworkException getException() {
            return exception;
        }

        public DataValidationException getValidationException() {
            return validationException;
        }

        public boolean isHasError() {
            return hasError;
        }
    }
    private static final class BuilderRunner implements Callable<DataContainer> {

        private List<DataBuilderExecutionListener> dataBuilderExecutionListener;
        private DataFlowInstance dataFlowInstance;
        private DataBuilderMeta builderMeta;
        private DataDelta dataDelta;
        private Map<String,Data> responseData;
        private DataBuilder builder;
        private DataBuilderContext dataBuilderContext;
        private final Set<DataBuilderMeta> procesedBuilders;
        private DataSet dataSet;

        private BuilderRunner(List<DataBuilderExecutionListener> dataBuilderExecutionListener,
                              DataFlowInstance dataFlowInstance,
                              DataBuilderMeta builderMeta,
                              DataDelta dataDelta,
                              Map<String, Data> responseData,
                              DataBuilder builder,
                              DataBuilderContext dataBuilderContext,
                              Set<DataBuilderMeta> procesedBuilders,
                              DataSet dataSet) {
            this.dataBuilderExecutionListener = dataBuilderExecutionListener;
            this.dataFlowInstance = dataFlowInstance;
            this.builderMeta = builderMeta;
            this.dataDelta = dataDelta;
            this.responseData = responseData;
            this.builder = builder;
            this.dataBuilderContext = dataBuilderContext;
            this.procesedBuilders = procesedBuilders;
            this.dataSet = dataSet;
        }

        @Override
        public DataContainer call() throws Exception {

            for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                try {
                    listener.beforeExecute(dataFlowInstance, builderMeta, dataDelta, responseData);
                } catch (Throwable t) {
                    logger.error("Error running pre-execution execution listener: ", t);
                }
            }
            try {
                Data response = builder.process(dataBuilderContext.immutableCopy(
                                            dataSet.accessor().getAccesibleDataSetFor(builder)));
                //logger.debug("Ran " + builderMeta.getName());
                procesedBuilders.add(builderMeta);
                for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                    try {
                        listener.afterExecute(dataFlowInstance, builderMeta, dataDelta, responseData, response);
                    } catch (Throwable t) {
                        logger.error("Error running post-execution listener: ", t);
                    }
                }
                if(null != response) {
                    Preconditions.checkArgument(response.getData().equalsIgnoreCase(builderMeta.getProduces()),
                            String.format("Builder is supposed to produce %s but produces %s",
                                    builderMeta.getProduces(), response.getData()));
                    response.setGeneratedBy(builderMeta.getName());
                }
                return new DataContainer(builderMeta, response);
            } catch (DataBuilderException e) {
                logger.error("Error running builder: " + builderMeta.getName());
                for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                    try {
                        listener.afterException(dataFlowInstance, builderMeta, dataDelta, responseData, e);

                    } catch (Throwable error) {
                        logger.error("Error running post-execution listener: ", error);
                    }
                }
                return new DataContainer(builderMeta, new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR,
                        "Error running builder: " + builderMeta.getName(), e.getDetails(), e));

            } catch (DataValidationException e) {
                logger.error("Validation error in data produced by builder" +builderMeta.getName());
                for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                    try {
                        listener.afterException(dataFlowInstance, builderMeta, dataDelta, responseData, e);

                    } catch (Throwable error) {
                        logger.error("Error running post-execution listener: ", error);
                    }
                }
                return new DataContainer(builderMeta, new DataValidationException(DataValidationException.ErrorCode.DATA_VALIDATION_EXCEPTION,
                        "Error running builder: " + builderMeta.getName(), new DataExecutionResponse(responseData), e.getDetails(), e));


            }
            catch (Throwable t) {
                logger.error("Error running builder: " + builderMeta.getName());
                for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                    try {
                        listener.afterException(dataFlowInstance, builderMeta, dataDelta, responseData, t);

                    } catch (Throwable error) {
                        logger.error("Error running post-execution listener: ", error);
                    }
                }
                Map<String, Object> objectMap = new HashMap<String, Object>();
                objectMap.put("MESSAGE", t.getMessage());
                return new DataContainer(builderMeta, new DataBuilderFrameworkException(DataBuilderFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR,
                        "Error running builder: " + builderMeta.getName() + t.getMessage(), objectMap, t));
            }
        }
    }

}
