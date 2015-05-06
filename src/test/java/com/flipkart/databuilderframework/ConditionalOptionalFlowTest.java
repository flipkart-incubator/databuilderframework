package com.flipkart.databuilderframework;


import com.flipkart.databuilderframework.engine.*;
import com.flipkart.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import com.flipkart.databuilderframework.model.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConditionalOptionalFlowTest {
	private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();
	private DataFlowExecutor executor = new SimpleDataFlowExecutor(new InstantiatingDataBuilderFactory(dataBuilderMetadataManager));
	private ExecutionGraphGenerator executionGraphGenerator = new ExecutionGraphGenerator(dataBuilderMetadataManager);
	private DataFlow dataFlow;
	private DataFlow dataFlowError = new DataFlow();

	// conditional builder here runs if C and D are present.
	// if C == "Hello World"
	// and D == "this" or G is present and G == this
	public static final class ConditionalBuilder extends DataBuilder {

		@Override
		public Data process(DataBuilderContext context) throws DataBuilderException {
			DataSetAccessor accessor = new DataSetAccessor(context.getDataSet());
			TestDataC dataC = accessor.get("C", TestDataC.class);
			TestDataD dataD = accessor.get("D", TestDataD.class);
			Optional<TestDataG> dataGOptional = accessor.getOptional("G",TestDataG.class);

			if(dataC.getValue().equals("Hello World")){
				if( dataD.getValue().equalsIgnoreCase("this")) {
					return new TestDataE("Wah wah!!");
				}else if(dataGOptional.isPresent()){
					TestDataG dataG = dataGOptional.get();
					if(dataG.getValue().equalsIgnoreCase("this")){
						return new TestDataE("Wah wah!!");
					}
				}

			}
			return null;
		}
	}

	@Before
	public void setup() throws Exception {
		dataBuilderMetadataManager.register(ImmutableSet.of("A", "B"), "C", "BuilderA", TestBuilderA.class ); //concats A and B values
		dataBuilderMetadataManager.register(ImmutableSet.of("C", "D"),ImmutableSet.of("G"), "E", "BuilderB", ConditionalBuilder.class );
		dataBuilderMetadataManager.register(ImmutableSet.of("A", "E"), "F", "BuilderC", TestBuilderC.class );

		dataFlow = new DataFlowBuilder()
		.withMetaDataManager(dataBuilderMetadataManager)
		.withTargetData("F")
		.build();

		dataFlowError = new DataFlowBuilder()
		.withMetaDataManager(dataBuilderMetadataManager)
		.withTargetData("Y")
		.build();

	}

	@Test
	public void testRunWithOptional() throws Exception {
		DataFlowInstance dataFlowInstance = new DataFlowInstance();
		dataFlowInstance.setId("testflow");
		dataFlowInstance.setDataFlow(dataFlow);
		{
			DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataA("Hello"), new TestDataB("World"), new TestDataD("notThis")));
			DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
			Assert.assertTrue(response.getResponses().containsKey("C"));
			Assert.assertFalse(response.getResponses().containsKey("E"));
		}
		{
			DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataG("notThis")));
			DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
			Assert.assertFalse(response.getResponses().containsKey("E"));
			Assert.assertFalse(response.getResponses().containsKey("C"));
		}
		{
			DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataG("this")));
			DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
			Assert.assertTrue(response.getResponses().containsKey("E"));
			Assert.assertTrue(response.getResponses().containsKey("F"));
		}
		{
			DataDelta dataDelta = new DataDelta(Lists.<Data>newArrayList(new TestDataD("this")));
			DataExecutionResponse response = executor.run(dataFlowInstance, dataDelta);
			Assert.assertTrue(response.getResponses().containsKey("E"));
			Assert.assertTrue(response.getResponses().containsKey("F"));
		}

	}

}
