package io.appform.databuilderframework.cmplxscenariotest.builders;

import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.cmplxscenariotest.ThreadUtils;
import io.appform.databuilderframework.cmplxscenariotest.data.DataA;
import io.appform.databuilderframework.cmplxscenariotest.data.DataA1;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.engine.DataValidationException;
import io.appform.databuilderframework.model.Data;
import io.appform.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderA1", consumes = {"A"}, produces = "A1")
public class BuilderA1 extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		DataSetAccessor dataSetAccessor = DataSet.accessor(context.getDataSet());
		DataA dataA = dataSetAccessor.get("A", DataA.class);
		ThreadUtils.INSTANCE.putToSleep(20, "A1");
		if(dataA.val <= 7){ //70% of case this builder will run and genrate data
			return new DataA1();
		}
		
		return null;
	}

}
