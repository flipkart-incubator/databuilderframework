package com.flipkart.cp.convert.europa.databuilderframework.engine;

import com.flipkart.cp.convert.europa.databuilderframework.model.*;
import com.flipkart.cp.convert.europa.databuilderframework.util.DataSetAccessor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * The executor for a {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataFlow}.
 */
public class MultiThreadedDataFlowExecutor extends DataFlowExecutor {
    private static final Logger logger = LoggerFactory.getLogger(MultiThreadedDataFlowExecutor.class.getSimpleName());
    private final ExecutorService executorService;

    public MultiThreadedDataFlowExecutor(DataBuilderFactory dataBuilderFactory, ExecutorService executorService) {
        super(dataBuilderFactory);
        this.executorService = executorService;
    }

    /**
     * {@inheritDoc}
     */
    public DataExecutionResponse run(DataBuilderContext dataBuilderContext, DataFlowInstance dataFlowInstance, DataDelta dataDelta) throws DataFrameworkException {
        CompletionService<DataContainer> completionExecutor = new ExecutorCompletionService<DataContainer>(executorService);
        DataFlow dataFlow = dataFlowInstance.getDataFlow();
        ExecutionGraph executionGraph = dataFlow.getExecutionGraph();
        DataSet dataSet = dataFlowInstance.getDataSet().accessor().copy(); //Create own copy to work with
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        dataSetAccessor.merge(dataDelta);
        dataBuilderContext.setDataSet(dataSet);
        Map<String, Data> responseData = Maps.newTreeMap();
        Set<String> activeDataSet = Sets.newHashSet();

        for (Data data : dataDelta.getDelta()) {
            activeDataSet.add(data.getData());
        }
        List<List<DataBuilderMeta>> dependencyHierarchy = executionGraph.getDependencyHierarchy();
        Set<String> newlyGeneratedData = Sets.newHashSet();
        while(true) {
            for (List<DataBuilderMeta> levelBuilders : dependencyHierarchy) {
                List<Future<DataContainer>> dataFutures = Lists.newArrayList();
                for (DataBuilderMeta builderMeta : levelBuilders) {
                    if (builderMeta.isProcessed()) {
                        continue;
                    }
                    Set<String> intersection = new HashSet<String>(builderMeta.getConsumes());
                    intersection.retainAll(activeDataSet);
                    //If there is an intersection, means some of it's inputs have changed. Reevaluate
                    if (intersection.isEmpty()) {
                        continue;
                    }
                    DataBuilder builder = dataBuilderFactory.create(builderMeta.getName());
                    if (!dataSetAccessor.checkForData(builder.getDataBuilderMeta().getConsumes())) {
                        break; //No need to run others, list is topo sorted
                    }
                    BuilderRunner builderRunner = new BuilderRunner(dataBuilderExecutionListener, dataFlowInstance,
                                                                        builderMeta, dataDelta, responseData,
                                                                        builder, dataBuilderContext);
                    Future<DataContainer> future = completionExecutor.submit(builderRunner);
                    dataFutures.add(future);
                }

                //Now wait for something to complete.
                for(int i = 0; i < dataFutures.size(); i++) {
                    try {
                        DataContainer responseContainer = completionExecutor.take().get();
                        Data response = responseContainer.getGeneratedData();
                        if(responseContainer.isHasError()) {
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
                    } catch (InterruptedException e) {
                        throw new DataFrameworkException(DataFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR,
                                "Error while waiting for error ", e);
                    } catch (ExecutionException e) {
                        throw new DataFrameworkException(DataFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR,
                                "Error while waiting for error ", e.getCause());
                    }
                }
            }
            if(newlyGeneratedData.contains(dataFlow.getTargetData())) {
                logger.debug("Finished running this instance of the flow. Exiting.");
                break;
            }
            if(newlyGeneratedData.isEmpty()) {
                logger.debug("Nothing happened in this loop, exiting..");
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
        DataSet finalDataSet = dataSetAccessor.copy(dataFlowInstance.getDataFlow().getTransients());
        dataFlowInstance.setDataSet(finalDataSet);
        return new DataExecutionResponse(responseData);
    }

    private static final class DataContainer {
        private final DataBuilderMeta builderMeta;
        private final Data generatedData;
        private boolean hasError = false;
        private final DataFrameworkException exception;

        private DataContainer(DataBuilderMeta builderMeta, Data generatedData) {
            this.builderMeta = builderMeta;
            this.generatedData = generatedData;
            this.exception = null;
        }

        private DataContainer(DataBuilderMeta builderMeta, DataFrameworkException exception) {
            this.builderMeta = builderMeta;
            this.generatedData = null;
            this.hasError = true;
            this.exception = exception;
        }

        public DataBuilderMeta getBuilderMeta() {
            return builderMeta;
        }

        public Data getGeneratedData() {
            return generatedData;
        }

        public DataFrameworkException getException() {
            return exception;
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

        private BuilderRunner(List<DataBuilderExecutionListener> dataBuilderExecutionListener,
                              DataFlowInstance dataFlowInstance,
                              DataBuilderMeta builderMeta,
                              DataDelta dataDelta,
                              Map<String, Data> responseData,
                              DataBuilder builder,
                              DataBuilderContext dataBuilderContext) {
            this.dataBuilderExecutionListener = dataBuilderExecutionListener;
            this.dataFlowInstance = dataFlowInstance;
            this.builderMeta = builderMeta;
            this.dataDelta = dataDelta;
            this.responseData = responseData;
            this.builder = builder;
            this.dataBuilderContext = dataBuilderContext;
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
                Data response = builder.process(dataBuilderContext);
                logger.debug("Ran " + builderMeta.getName());
                builderMeta.setProcessed(true);
                for (DataBuilderExecutionListener listener : dataBuilderExecutionListener) {
                    try {
                        listener.afterExecute(dataFlowInstance, builderMeta, dataDelta, responseData, response);
                    } catch (Throwable t) {
                        logger.error("Error running post-execution listener: ", t);
                    }
                }
                if(null != response) {
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
                return new DataContainer(builderMeta, new DataFrameworkException(DataFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR,
                        "Error running builder: " + builderMeta.getName(), e.getData(), e));

            } catch (Throwable t) {
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
                return new DataContainer(builderMeta, new DataFrameworkException(DataFrameworkException.ErrorCode.BUILDER_EXECUTION_ERROR,
                        "Error running builder: " + builderMeta.getName() + t.getMessage(), objectMap, t));
            }
        }
    }

}
