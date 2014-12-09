package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.model.DataBuilderMeta;
import com.flipkart.databuilderframework.model.DataFlow;
import com.flipkart.databuilderframework.model.ExecutionGraph;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This class generates an {@link com.flipkart.databuilderframework.model.ExecutionGraph}.
 * It uses the target data and resolution spec provided in the {@link com.flipkart.databuilderframework.model.DataFlow}
 * to generate a dependency list. This is used later by the {@link DataFlowExecutor}
 * to run the flow.
 */
public class DataFlowBuilder {
    private static final Logger logger = LoggerFactory.getLogger(DataFlowBuilder.class.getSimpleName());

    private DataBuilderMetadataManager dataBuilderMetadataManager;

    public DataFlowBuilder(DataBuilderMetadataManager dataBuilderMetadataManager) {
        this.dataBuilderMetadataManager = dataBuilderMetadataManager;
    }

    /**
     * Generates an {@link com.flipkart.databuilderframework.model.ExecutionGraph} for the given graph.
     * An exception is thrown if not target is specified, or there are multiple builders for the same data, but no
     * resolution is provided for the same(conflict).
     * @param dataFlow The {@link com.flipkart.databuilderframework.model.DataFlow} object to be analyzed
     * @return Returns the ExecutionGraph
     * @throws DataFrameworkException
     */
    public ExecutionGraph generateGraph(final DataFlow dataFlow) throws DataFrameworkException {
        if(dataFlow.getTargetData() == null || dataFlow.getTargetData().isEmpty()) {
            throw new DataFrameworkException(DataFrameworkException.ErrorCode.NO_TARGET_DATA,
                                                                    "No target data specified for flow");
        }
        DependencyInfoManager dependencyInfoManager = new DependencyInfoManager();

        /**
         * STEP 1:: GENERATE DEPENDENCY TREE {ROOT=>TARGET}
         */
        DependencyNode root = generateDependencyTree(dataFlow.getTargetData(), dataFlow, null,
                new DependencyNodeManager(), dependencyInfoManager,
                new FlattenedDataRoute());
        /**
         * STEP 2:: RANK NODES IN THE TREE ACCORDING TO DISTANCE FROM ROOT
         */
        int maxHeight = rankNodes(root, 0);

        /**
        STEP 3:: CREATE REPRESENTATION
        Representation : Example of a three level tree:
            A
            |
            +--B
            |  |
            |  +--C
            |  |
            |  +--D
            +--E
               |
               +--F
               |
               +--G
        Dependency Hierarchy:
        [
           0 : [C, D, F, G]
           1 : [B, E]
           2 : [A]
        ]
        */
        Map<String, DependencyInfo> dependencyInfos = dependencyInfoManager.infos;

        //Fill up array with nulls. Array size == max Rank
        List<List<DataBuilderMeta>> dependencyHierarchy
                                        = new ArrayList<List<DataBuilderMeta>>(
                                                    Collections.<List<DataBuilderMeta>>nCopies(maxHeight + 1, null));

        //For each dependency
        for(Map.Entry<String, DependencyInfo> dependencyInfo : dependencyInfos.entrySet()) {
            DataBuilderMeta tmpDataBuilderMeta
                                = dataBuilderMetadataManager.get(dependencyInfo.getValue().getBuilder());
            if(null == tmpDataBuilderMeta) {
                //Data is user-input data
                continue;
            }
            DataBuilderMeta dataBuilderMeta = tmpDataBuilderMeta.deepCopy();
            int rank = dependencyInfo.getValue().getRank();
            dataBuilderMeta.setRank(rank);

            //Set builder in the appropriate rank slots
            if(null == dependencyHierarchy.get(rank)) {
                dependencyHierarchy.set(rank, Lists.<DataBuilderMeta>newArrayList());
            }
            dependencyHierarchy.get(rank).add(dataBuilderMeta);
        }

        //A few levels will be null as they are built exclusively of user-input data
        //Remove these useless ranks
        dependencyHierarchy.removeAll(Collections.singleton(null));

        //Reverse the array for helping in bottom up traversal during execution
        Collections.reverse(dependencyHierarchy);

        //Return
        return new ExecutionGraph(dependencyHierarchy);
    }

    private int rankNodes(DependencyNode root, int currentNode) {
        if(root.getData().getRank() < currentNode) {
            root.getData().setRank(currentNode);
        }
        int childNode = currentNode + 1;
        int returnValue = childNode;
        for(DependencyNode child : root.getIncoming()) {
            int val = rankNodes(child, childNode);
            returnValue = (val > returnValue)?val:returnValue;
        }
        return returnValue;
    }

    private DependencyNode generateDependencyTree(final String data, DataFlow dataFlow,
                                                  DependencyInfo outgoing,
                                                  DependencyNodeManager dependencyNodeManager,
                                                  DependencyInfoManager dependencyInfoManager,
                                                  FlattenedDataRoute routeMeta) throws DataFrameworkException {
        logger.info("Generating for: " + data);
        List<DependencyNode> incoming = Lists.newArrayList();
        DataBuilderMeta dataBuilderMeta = findBuilder(data, dataFlow);
        DependencyInfo info = dependencyInfoManager.get(data);
        if(null == info.getData()) {
            info.setData(data);
        }
        if(null != dataBuilderMeta) {
            if(null == info.getBuilder()) {
                info.setBuilder(dataBuilderMeta.getName());
            }
            for(String consumes : dataBuilderMeta.getConsumes()) {
                if(routeMeta.isAlreadyOnOutgoingPath(data, consumes)) {
                    logger.info(String.format("Loop detected: Path for %s already contains %s", consumes, data));
                    continue;
                }
                routeMeta.addOutputData(consumes, data);
                DependencyNode childnode = generateDependencyTree(consumes, dataFlow, info,
                                                dependencyNodeManager, dependencyInfoManager, routeMeta);
                if(null != childnode) {
                    incoming.add(childnode);
                }
            }
        }
        DependencyNode root = dependencyNodeManager.get(data);
        if(root.getData() == null ){
            root.setData(info);
            root.setIncoming(incoming);
        }
        if(null != outgoing) {
            root.getOutgoing().add(outgoing);
        }
        return root;
    }

    private DataBuilderMeta findBuilder(String data, DataFlow dataFlow) throws DataFrameworkException {
        Map<String, String> resolutionSpecs = dataFlow.getResolutionSpecs();
        DataBuilderMeta producerMeta = null;
        if(null != resolutionSpecs && resolutionSpecs.containsKey(data)) {
            producerMeta = dataBuilderMetadataManager.get(resolutionSpecs.get(data));
            if(null == producerMeta) {
                //A resolution spec was specified but no builder was found
                throw new DataFrameworkException(DataFrameworkException.ErrorCode.NO_BUILDER_FOR_DATA,
                        "No builder found with name: " + resolutionSpecs.get(data));
            }
            //logger.info("Found builder for data " + data + ": " + resolutionSpecs.get(data));
        }
        if(null == producerMeta) {
            List<DataBuilderMeta> producerMetaList = dataBuilderMetadataManager.getMetaForProducerOf(data);
            if(producerMetaList == null) {
                logger.info("Starting data point found: " + data);
                return null;
            }

            if(producerMetaList.size() > 1) {
                //No resolution spec was specified, but multiple builders were found
                logger.error("Multiple builders found for data, but no resolution spec found. Cannot proceed. data: " + data);
                throw new DataFrameworkException(DataFrameworkException.ErrorCode.BUILDER_RESOLUTION_CONFLICT_FOR_DATA,
                        "Multiple builders found for data, but no resolution spec found. Cannot proceed. Data: " + data);
            }
            producerMeta = producerMetaList.get(0);
        }
        return producerMeta;
    }

    private static class FlattenedDataRoute {
        private Map<String, Set<String>> outgoingMap = Maps.newHashMap();

        public void addOutputData(String input, String output) {
            if(!outgoingMap.containsKey(input)) {
                outgoingMap.put(input, Sets.<String>newHashSet());
            }
            outgoingMap.get(input).add(output);
            if(outgoingMap.containsKey(output)) {
                outgoingMap.get(input).addAll(outgoingMap.get(output)); //My output's outputs are my outputs also
            }
            //logger.info(String.format("Added %s in path for %s", input, output));
        }

        public boolean isAlreadyOnOutgoingPath(String input, String output) {
            if(!outgoingMap.containsKey(input)) {
                return false;
            }
            return outgoingMap.get(input).contains(output);
        }
    }

    private static class DependencyInfo {
        private String data;
        private String builder;
        private int rank = 0;

        private DependencyInfo(String data, String builder) {
            this.data = data;
            this.builder = builder;
        }

        private DependencyInfo() {
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getBuilder() {
            return builder;
        }

        public void setBuilder(String builder) {
            this.builder = builder;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    private static final class DependencyInfoManager {
        private Map<String, DependencyInfo> infos = Maps.newHashMap();
        DependencyInfo get(String data) {
            if(!infos.containsKey(data)) {
                infos.put(data,new DependencyInfo());
            }
            return infos.get(data);
        }
    }

    private static class DependencyNode {

        private DependencyInfo data;
        private List<DependencyNode> incoming = Lists.newArrayList();
        private Set<DependencyInfo> outgoing = Sets.newLinkedHashSet();

        private DependencyNode() {
        }

        public DependencyInfo getData() {
            return data;
        }

        public void setData(DependencyInfo data) {
            this.data = data;
        }

        public List<DependencyNode> getIncoming() {
            return incoming;
        }

        public void setIncoming(List<DependencyNode> incoming) {
            this.incoming = incoming;
        }

        public Set<DependencyInfo> getOutgoing() {
            return outgoing;
        }

        public void setOutgoing(Set<DependencyInfo> outgoing) {
            this.outgoing = outgoing;
        }
    }

    private static class DependencyNodeManager {
        private Map<String, DependencyNode> dataList = Maps.newHashMap();

        DependencyNode get(String data) {
            if(!dataList.containsKey(data)) {
                dataList.put(data, new DependencyNode());
            }
            return dataList.get(data);
        }

    }
}
