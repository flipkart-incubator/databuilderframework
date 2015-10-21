package com.flipkart.databuilderframework.cmplxscenariotest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderA1;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderA2;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderA3;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderB1;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderB2;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderB3;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderB4;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderB5;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderC;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderD;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderE1;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderE2;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderE3;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderE4;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderE5;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderE6;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderF;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderG;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderH;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderI;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderJ;
import com.flipkart.databuilderframework.cmplxscenariotest.builders.BuilderK;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataA;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataI;
import com.flipkart.databuilderframework.cmplxscenariotest.data.InputAData;
import com.flipkart.databuilderframework.engine.DataBuilderFrameworkException;
import com.flipkart.databuilderframework.engine.DataBuilderMetadataManager;
import com.flipkart.databuilderframework.engine.DataValidationException;
import com.flipkart.databuilderframework.engine.ExecutionGraphGenerator;
import com.flipkart.databuilderframework.engine.MultiThreadedDataFlowExecutor;
import com.flipkart.databuilderframework.engine.SimpleDataFlowExecutor;
import com.flipkart.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import com.flipkart.databuilderframework.model.DataExecutionResponse;
import com.flipkart.databuilderframework.model.DataFlow;
import com.flipkart.databuilderframework.model.DataFlowInstance;
import com.flipkart.databuilderframework.model.ExecutionGraph;
import com.google.common.collect.Sets;

public class TestBuilderOfSameRankNotBreakingInBetween {

	private final DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
	private final SimpleDataFlowExecutor se = new SimpleDataFlowExecutor(new InstantiatingDataBuilderFactory(dataBuilderMetadataManager));
//	private final ProfileExecutor builderExecutor = new ProfileExecutor(200, 250);
	private final ExecutorService builderExecutor  = Executors.newFixedThreadPool(200);
	private final MultiThreadedDataFlowExecutor me =  new MultiThreadedDataFlowExecutor(new InstantiatingDataBuilderFactory(dataBuilderMetadataManager),builderExecutor);;

	@Before
	public void setup() throws Exception {
		dataBuilderMetadataManager.register(BuilderA1.class);
		dataBuilderMetadataManager.register(BuilderA2.class);
		dataBuilderMetadataManager.register(BuilderA3.class);
		dataBuilderMetadataManager.register(BuilderB1.class);
		dataBuilderMetadataManager.register(BuilderB2.class);
		dataBuilderMetadataManager.register(BuilderB3.class);
		dataBuilderMetadataManager.register(BuilderB4.class);
		dataBuilderMetadataManager.register(BuilderB5.class);
		dataBuilderMetadataManager.register(BuilderC.class);
		dataBuilderMetadataManager.register(BuilderD.class);
		dataBuilderMetadataManager.register(BuilderE1.class);
		dataBuilderMetadataManager.register(BuilderE2.class);
		dataBuilderMetadataManager.register(BuilderE3.class);
		dataBuilderMetadataManager.register(BuilderE4.class);
		dataBuilderMetadataManager.register(BuilderE5.class);
		dataBuilderMetadataManager.register(BuilderE6.class);
		dataBuilderMetadataManager.register(BuilderF.class);
		dataBuilderMetadataManager.register(BuilderG.class);
		dataBuilderMetadataManager.register(BuilderH.class);
		dataBuilderMetadataManager.register(BuilderI.class);
		dataBuilderMetadataManager.register(BuilderJ.class);
		dataBuilderMetadataManager.register(BuilderK.class);
	}

	@Test
	public void testToCheckIfBuilderA2RunsEvenWhenRankIsLess() throws DataBuilderFrameworkException, DataValidationException{
		DataFlow dataflow = new DataFlow();
		dataflow.setDescription("Complex DataFlow");
		dataflow.setEnabled(true);
		dataflow.setTargetData("K");
		dataflow.setTransients(Sets.newHashSet("IA","I"));
		dataflow.setName("complext_flow");

		ExecutionGraphGenerator graphGenerator = new ExecutionGraphGenerator(dataBuilderMetadataManager);
		ExecutionGraph graph = graphGenerator.generateGraph(dataflow);
		dataflow.setExecutionGraph(graph);

		final DataFlow dataflowRef = dataflow;
		final DataFlowInstance instance  = new DataFlowInstance("test", dataflowRef);
		DataExecutionResponse resp = se.run(instance, new DataA(8), new InputAData());
		Assert.assertEquals(true,resp.getResponses().containsKey("K"));
		
		final DataFlowInstance meInstance  = new DataFlowInstance("test2", dataflowRef);
		DataExecutionResponse resp2 = me.run(instance, new DataA(8), new InputAData());
		Assert.assertEquals(true,resp2.getResponses().containsKey("K"));
	}
}
