package io.appform.databuilderframework.cmplxscenariotest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderA1;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderA2;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderA3;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderB1;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderB2;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderB3;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderB4;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderB5;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderC;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderD;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderE1;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderE2;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderE3;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderE4;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderE5;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderE6;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderF;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderG;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderH;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderI;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderJ;
import io.appform.databuilderframework.cmplxscenariotest.builders.BuilderK;
import io.appform.databuilderframework.cmplxscenariotest.data.DataI;
import io.appform.databuilderframework.cmplxscenariotest.data.InputAData;
import io.appform.databuilderframework.engine.*;
import io.appform.databuilderframework.engine.impl.InstantiatingDataBuilderFactory;
import io.appform.databuilderframework.model.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class ExecutionGraphGeneratorTest {
    private DataBuilderMetadataManager dataBuilderMetadataManager = new DataBuilderMetadataManager();


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
	public void testGraphGeneration() throws Exception{
		
		DataFlow dataflow = new DataFlow();
		dataflow.setDescription("Complex DataFlow");
		dataflow.setEnabled(true);
		dataflow.setTargetData("K");
		dataflow.setTransients(Sets.newHashSet("IA","I"));
		dataflow.setName("complext_flow");
		
		ExecutionGraphGenerator graphGenerator = new ExecutionGraphGenerator(dataBuilderMetadataManager);
		ExecutionGraph graph = graphGenerator.generateGraph(dataflow);
		dataflow.setExecutionGraph(graph);
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		log.info(mapper.writeValueAsString(dataflow));
	}

}
